package com.bjike.wl.iot.ui.activtiy

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.os.*
import com.bjike.issp.utils.showToast
import com.bjike.wl.iot.R
import com.bjike.wl.iot.mvp.contract.RegisterContract
import com.bjike.wl.iot.mvp.presenter.RegisterPresenter

import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

/**
 * A login screen that offers login via email/password.
 */
class RegisterActivity : AppCompatActivity(), LoaderCallbacks<Cursor>, RegisterContract.View {


    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Set up the login form.
        populateAutoComplete()
        presenter = RegisterPresenter(this@RegisterActivity, this@RegisterActivity)
        et_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        tv_security_code.setOnClickListener(View.OnClickListener { if (!isKeepTime) haveSecurityCode() })
        btn_email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(actv_email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }

    /**
     * 获取验证码
     */
    fun haveSecurityCode() {
        var cancel = false
        var focusView: View? = null
        val emailStr = actv_email.text.toString()
        if (TextUtils.isEmpty(emailStr)) {
            actv_email.error = getString(R.string.error_field_required)
            focusView = actv_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            actv_email.error = getString(R.string.error_invalid_email)
            focusView = actv_email
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()

        } else {
            var fieldMap = HashMap<String, String>(0)
            fieldMap.put("email", actv_email.text.toString())
            presenter.getCode(fieldMap)
        }
    }

    private var time = 120
    var isKeepTime: Boolean = false //是否计时中


    /**
     * 验证码时间倒计时
     */
    internal var handlerText: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                if (time > 0) {
                    tv_security_code.text = "" + time + "秒"
                    time--
                    this.sendEmptyMessageDelayed(1, 1000)
                } else {
                    tv_security_code.text = "验证码"
                    isKeepTime = false
                    time = 120
                }
            } else {
                //  T.showShort(CommonRegisterActivity.this,"验证通过`");
            }
        }
    }

    /**
     * 注册
     */
    private fun attemptLogin() {

        // Reset errors.
        actv_email.error = null
        et_password.error = null

        // Store values at the time of the login attempt.
        val emailStr = actv_email.text.toString()
        val passwordStr = et_password.text.toString()
        val affirm_password = et_affirm_password.text.toString()
        val security_code = et_security_code.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(security_code)) {
            et_security_code.error = getString(R.string.error_field_required)
            focusView = et_security_code
            cancel = true
        } else if (!isSecurityCodeValid(security_code)) {
            et_security_code.error = getString(R.string.error_security_code)
            focusView = et_security_code
            cancel = true
        }

        if (TextUtils.isEmpty(affirm_password)) {
            et_affirm_password.error = getString(R.string.error_invalid_password)
            focusView = et_affirm_password
            cancel = true
        } else if (!isPasswordValid(affirm_password)) {
            et_affirm_password.error = getString(R.string.error_incorrect_password)
            focusView = et_affirm_password
            cancel = true
        }
        if (passwordStr != affirm_password) {
            et_affirm_password.error = getString(R.string.error_affirm_password)
            focusView = et_affirm_password
            cancel = true
        }
        if (TextUtils.isEmpty(passwordStr)) {
            et_password.error = getString(R.string.error_invalid_password)
            focusView = et_password
            cancel = true
        } else if (!isPasswordValid(passwordStr)) {
            et_password.error = getString(R.string.error_incorrect_password)
            focusView = et_password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            actv_email.error = getString(R.string.error_field_required)
            focusView = actv_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            actv_email.error = getString(R.string.error_invalid_email)
            focusView = actv_email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            var fieldMap = HashMap<String, String>(0)
            fieldMap.put("email", actv_email.text.toString())
            fieldMap.put("password", et_password.text.toString())
            fieldMap.put("captcha", et_security_code.text.toString())
            presenter.postRegister(fieldMap)
        }
    }

    val REGEX_EMAIL = "^^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return Pattern.matches(REGEX_EMAIL, email)
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 5
    }

    private fun isSecurityCodeValid(securityCode: String): Boolean {
        //TODO: Replace this with your own logic
        return securityCode.length == 5
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            sv_login_form.visibility = if (show) View.GONE else View.VISIBLE
            sv_login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            sv_login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            pb_login_progress.visibility = if (show) View.VISIBLE else View.GONE
            pb_login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            pb_login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pb_login_progress.visibility = if (show) View.VISIBLE else View.GONE
            sv_login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@RegisterActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        actv_email.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }


    /**
     * 验证码获取成功倒计时
     */
    override fun getCode(results: String) {
        isKeepTime = true
        handlerText.sendEmptyMessageDelayed(1, 1000)

    }

    override fun postRegister(results: String) {
        showToast(results)
        showProgress(false)
        this@RegisterActivity.finish()
    }

    override fun showError(errorString: String) {
        showProgress(false)
        showToast(errorString)

    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}

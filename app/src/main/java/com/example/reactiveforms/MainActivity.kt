package com.example.reactiveforms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.reactiveforms.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    var getEmailMutableLiveData = MutableLiveData<Boolean>()
    fun getEmailLiveData(): LiveData<Boolean?>? {
        return getEmailMutableLiveData
    }
    var getPasswordMutableLiveData = MutableLiveData<Boolean>()
    fun getPasswordLiveData():LiveData<Boolean?>?{
        return getPasswordMutableLiveData
    }
    var getConfirmPasswordMutableLiveData = MutableLiveData<Boolean>()
    fun getConfirmPasswordLiveData():LiveData<Boolean?>?{
        return getConfirmPasswordMutableLiveData
    }
    var password = MutableLiveData<CharSequence>()
    var check = MutableLiveData<Boolean>()

    var passwordObservable:Observable<Boolean>?=null
    var confirmPasswordObservable: Observable<Boolean>? = null
    var emailObservable: Observable<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpUi()
        setUpObserver()

    }

    private fun setUpObserver() {
        getEmailMutableLiveData.value = false
        getPasswordMutableLiveData.value = false
        getConfirmPasswordMutableLiveData.value =false

        getEmailLiveData()?.observe(this, Observer { boolean ->
            check.value = true
        })
        getPasswordLiveData()?.observe(this, Observer { boolean ->
            check.value = true
        })
        getConfirmPasswordMutableLiveData.observe(this, Observer { boolean ->
            check.value = true
        })
        check.observe(this, Observer { check ->
            if (getEmailLiveData()!!.value!! && getPasswordLiveData()!!.value!! && getConfirmPasswordLiveData()!!.value!!) {
                binding!!.submet.isEnabled = true
            } else binding!!.submet!!.isEnabled = false
        })

    }

    private fun setUpUi() {

//        binding.submet.setEnabled(false);

//        binding.submet.setEnabled(false);
        val sliderAdapter = SliderAdapter()
        binding?.imageSlider?.setSliderAdapter(sliderAdapter)

        binding!!.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding!!.emailInputLayout.error = "Invalid email"
                emailObservable = binding!!.email.textChanges()
                    .map { inputText: CharSequence ->
                        inputText.toString()
                            .matches("(?:[a-z0-9!#$%'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex())
                    }
                    .distinctUntilChanged()
                emailObservable?.subscribe(Consumer { isValid: Boolean? ->
                    binding!!.emailInputLayout.isErrorEnabled = !isValid!!
                })
                emailObservable?.subscribe(Consumer { isValid: Boolean? ->
                    getEmailMutableLiveData.postValue(
                        isValid
                    )
                })
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding!!.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password.postValue(binding?.password?.text)
                binding!!.passwodInputLayout.error = "Invalid password"
                passwordObservable = binding!!.password.textChanges()
                    .map { inputText: CharSequence ->
                        inputText.toString().matches("^(?=.*\\d).{4,8}$".toRegex())
                    }
                    .distinctUntilChanged()
                passwordObservable?.subscribe({ isVaild: Boolean? ->
                    binding!!.passwodInputLayout.isErrorEnabled = !isVaild!!
                })
                passwordObservable?.subscribe({ isValid: Boolean? ->
                    getPasswordMutableLiveData.postValue(
                        isValid
                    )
                })
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding!!.confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding!!.confirmPasswodInputLayout.error = "Invalid password"
                confirmPasswordObservable = binding!!.confirmPassword.textChanges()
                    .map { inputText: CharSequence ->
                        inputText.toString().equals(password.value.toString())
                    }
                    .distinctUntilChanged()
                confirmPasswordObservable?.subscribe({ isVaild: Boolean? ->
                    binding!!.confirmPasswodInputLayout.isErrorEnabled = !isVaild!!
                })
                confirmPasswordObservable?.subscribe({ isValid: Boolean? ->
                    getConfirmPasswordMutableLiveData.postValue(
                        isValid
                    )
                })
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


}
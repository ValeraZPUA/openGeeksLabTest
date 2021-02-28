package com.example.opengeekslabtest.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.opengeekslabtest.Constants
import com.example.opengeekslabtest.R
import com.example.opengeekslabtest.databinding.ActivityMainBinding
import com.example.opengeekslabtest.ui.dialogs.LoaderDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : MvpAppCompatActivity(), MainView, AdapterView.OnItemSelectedListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var binding: ActivityMainBinding
    private lateinit var spinnerRegion: Spinner
    private lateinit var loader: LoaderDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initLoader()
        configRegionSpinner()
    }

    private fun initLoader() {
        loader = LoaderDialogFragment()
        loader.isCancelable = false
    }

    private fun configRegionSpinner() {
        spinnerRegion = spinner_region
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.drop_down_locales))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegion.adapter = spinnerAdapter

        Locale.getDefault().country.let {
            binding.region = it
            spinnerRegion.setSelection(getIndex(it))
        }
        spinnerRegion.onItemSelectedListener = this
    }

    private fun getIndex(region: String): Int {
        for (i in 0 until spinnerRegion.count) {
            if (spinnerRegion.getItemAtPosition(i).toString() == region) {
                return i
            }
        }
        return 0
    }

    override fun setHelloMessage(helloMessage: String) {
        binding.helloMessage = helloMessage
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        showLoader()
        binding.region = spinnerRegion.getItemAtPosition(position).toString()
        presenter.findAliveUrl(
            when (spinnerRegion.selectedItem.toString().toLowerCase(Locale.getDefault())) {
                Constants.UA -> resources.getStringArray(R.array.locale_urls_ua)
                Constants.RU -> resources.getStringArray(R.array.locale_urls_ru)
                Constants.FR -> resources.getStringArray(R.array.locale_urls_fr)
                else -> resources.getStringArray(R.array.locale_urls_us)
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    private fun showLoader() {
        loader.show(supportFragmentManager, Constants.TAG_DIALOG_LOADER)
    }

    override fun hideLoader() {
        loader.dismiss()
    }

    override fun showNoAliveUrlsFoundToast() {
        Toast.makeText(this, getString(R.string.error_no_alive_urls), Toast.LENGTH_LONG).show()
    }

    override fun showErrorToast() {
        Toast.makeText(this, getString(R.string.error_something_went_wrong), Toast.LENGTH_LONG).show()
    }
}
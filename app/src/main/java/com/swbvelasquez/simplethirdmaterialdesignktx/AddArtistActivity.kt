package com.swbvelasquez.simplethirdmaterialdesignktx

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.swbvelasquez.simplethirdmaterialdesignktx.databinding.ActivityAddArtistBinding
import com.swbvelasquez.simplethirdmaterialdesignktx.dialogs.DateSelectorDialog
import com.swbvelasquez.simplethirdmaterialdesignktx.entities.Artist
import com.swbvelasquez.simplethirdmaterialdesignktx.utils.Constants
import com.swbvelasquez.simplethirdmaterialdesignktx.utils.Functions.toJson
import java.text.SimpleDateFormat
import java.util.*


class AddArtistActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityAddArtistBinding
    private lateinit var mCalendar: Calendar
    private var order:Int = 0
    private var birthDayArtist:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCalendar()
        getIntentExtras()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_save -> saveArtist()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        mCalendar.timeInMillis = System.currentTimeMillis()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = day

        binding.etBirthDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(mCalendar.timeInMillis))
        birthDayArtist = mCalendar.timeInMillis
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbMain)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT)
        birthDayArtist = System.currentTimeMillis()
        binding.etBirthDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(System.currentTimeMillis()))
        binding.etBirthDate.setOnClickListener {
            val datePicker = DateSelectorDialog()
            datePicker.setListener(this@AddArtistActivity)

            val args = Bundle()
            args.putLong(DateSelectorDialog.DATE_PARAM, birthDayArtist)

            datePicker.arguments = args
            datePicker.show(supportFragmentManager, DateSelectorDialog.SELECTED_DATE)
        }
    }

    private fun getIntentExtras(){
        order = intent.getIntExtra(Constants.NEW_ID_ARTIST_PARAM,0)
    }

    private fun validateFields(): Boolean {
        var isValid = true

        with(binding){
            if (etHeight.text != null && (etHeight.text.toString().trim { it <= ' ' }.isEmpty() ||
                        Integer.valueOf(etHeight.text.toString().trim { it <= ' ' }) < resources.getInteger(R.integer.estatura_min))) {
                tilHeight.error = getString(R.string.addArtist_error_estaturaMin)
                tilHeight.requestFocus()
                isValid = false
            }

            if (etLastName.text != null && etLastName.text.toString().trim { it <= ' ' }.isEmpty()) {
                tilLastName.error = getString(R.string.addArtist_error_required)
                tilLastName.requestFocus()
                isValid = false
            }

            if (etFirstName.text != null && etFirstName.text.toString().trim { it <= ' ' }.isEmpty()) {
                tilFirstName.error = getString(R.string.addArtist_error_required)
                tilFirstName.requestFocus()
                isValid = false
            }else{
                tilFirstName.error = null
            }
        }

        return isValid
    }

    private fun saveArtist(){
        if(validateFields()) {

            var artist: Artist

            with(binding) {
                artist = Artist(
                    id = order.toLong(),
                    firstName = etFirstName.text.toString().trim(),
                    lastName = etLastName.text.toString().trim(),
                    birthDate = birthDayArtist,
                    birthPlace = etBirthPlace.text.toString().trim(),
                    height = etHeight.text.toString().trim().toInt(),
                    notes = etNotes.text.toString().trim(),
                    order = order,
                    photoUrl = ""
                )
            }

            val resultIntent = Intent()
            resultIntent.putExtra(Constants.ARTIST_PARAM, artist.toJson())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
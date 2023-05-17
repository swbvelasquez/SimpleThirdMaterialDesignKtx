package com.swbvelasquez.simplethirdmaterialdesignktx

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.swbvelasquez.simplethirdmaterialdesignktx.databinding.ActivityDetailBinding
import com.swbvelasquez.simplethirdmaterialdesignktx.dialogs.DateSelectorDialog
import com.swbvelasquez.simplethirdmaterialdesignktx.entities.Artist
import com.swbvelasquez.simplethirdmaterialdesignktx.utils.Constants
import com.swbvelasquez.simplethirdmaterialdesignktx.utils.Functions.fromJson
import com.swbvelasquez.simplethirdmaterialdesignktx.utils.Functions.toJson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mCalendar: Calendar
    private lateinit var artist:Artist
    private var mMenuItem: MenuItem? = null
    private var mIsEdit = false
    private var birthDayArtist:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCalendar()
        getIntentExtras()
        setupButtons()
       // enableUIElements(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        mMenuItem = menu.findItem(R.id.action_save)
        mMenuItem?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_save -> {
                if (mIsEdit) {
                    if (validateFields()) {
                        saveOrEdit()
                        enableUIElements(false)
                        binding.fabSave.setImageResource(R.drawable.ic_account_edit)
                        mIsEdit = false
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        mCalendar.timeInMillis = System.currentTimeMillis()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = day

        binding.lyContentDetail.etBirthDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(mCalendar.timeInMillis))
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
        binding.lyContentDetail.etBirthDate.setOnClickListener {
            val datePicker = DateSelectorDialog()
            datePicker.setListener(this@DetailActivity)

            val args = Bundle()
            args.putLong(DateSelectorDialog.DATE_PARAM, birthDayArtist)

            datePicker.arguments = args
            datePicker.show(supportFragmentManager, DateSelectorDialog.SELECTED_DATE)
        }
    }

    private fun setupButtons(){
        binding.fabSave.setOnClickListener {
            if(mIsEdit){
                if(validateFields()) {
                    saveOrEdit()
                    enableUIElements(false)
                    binding.fabSave.setImageResource(R.drawable.ic_account_edit)
                    mIsEdit = false
                }
            }else{
                mIsEdit = true
                binding.fabSave.setImageResource(R.drawable.ic_account_check)
                enableUIElements(true)
            }
        }
    }

    private fun getIntentExtras(){
        val jsonArtist = intent.getStringExtra(Constants.ARTIST_PARAM)

        jsonArtist?.let {
            artist = jsonArtist.fromJson()
            setupViewData()
        }
    }

    private fun setupViewData(){
        with(binding.lyContentDetail){
            etFirstName.setText(artist.firstName)
            etLastName.setText(artist.lastName)
            etBirthDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(artist.birthDate))
            etAge.setText(getAge(artist.birthDate))
            etHeight.setText(artist.height.toString())
            etOrder.setText(artist.order.toString())
            etBirthPlace.setText(artist.birthPlace)
            etNotes.setText(artist.notes)

            binding.cblMain.title = artist.fullName
            birthDayArtist = artist.birthDate

            if(artist.photoUrl.isNotEmpty()) {
                Glide
                    .with(this@DetailActivity)
                    .load(artist.photoUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_sentiment_satisfied)
                    .into(binding.imvPhoto)
            }else{
                binding.imvPhoto.setImageResource(R.drawable.ic_photo_size_select_actual)
            }
        }
    }

    private fun getAge(birthDate: Long): String {
        val time = Calendar.getInstance().timeInMillis / 1000 - birthDate / 1000
        val years = time.toFloat().roundToInt() / 31536000
        return years.toString()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        with(binding.lyContentDetail){
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

    private fun enableUIElements(enable: Boolean) {
        with(binding.lyContentDetail) {
            etFirstName.isEnabled = enable
            etLastName.isEnabled = enable
            etBirthDate.isEnabled = enable
            etHeight.isEnabled = enable
            etBirthPlace.isEnabled = enable
            etNotes.isEnabled = enable
            mMenuItem!!.isVisible = enable

            containerMain.isNestedScrollingEnabled = !enable
        }

        binding.ablMain.setExpanded(!enable)
    }

    private fun saveOrEdit(){
        with(binding.lyContentDetail) {
            artist.firstName = etFirstName.text.toString().trim()
            artist.lastName = etLastName.text.toString().trim()
            artist.birthDate = birthDayArtist
            artist.birthPlace = etBirthPlace.text.toString().trim()
            artist.height = etHeight.text.toString().trim().toInt()
            artist.notes = etNotes.text.toString().trim()

            val resultIntent = Intent()
            resultIntent.putExtra(Constants.ARTIST_PARAM, artist.toJson())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
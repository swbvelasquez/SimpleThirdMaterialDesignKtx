package com.swbvelasquez.simplethirdmaterialdesignktx

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private var lastPhotoUrl:String?=null

    private val openGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){ result ->
        if(result != null){
            setImageFromUrl(result.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCalendar()
        setupButtons()
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

    private fun setupButtons(){
        binding.imvFromUrl.setOnClickListener {
            showInputUrlDialog()
        }
        binding.imvFromGallery.setOnClickListener {
            showGallery()
        }
        binding.imvDeletePhoto.setOnClickListener {
            showDeletePhotoDialog()
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
                    photoUrl = lastPhotoUrl?:""
                )
            }

            val resultIntent = Intent()
            resultIntent.putExtra(Constants.ARTIST_PARAM, artist.toJson())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun showInputUrlDialog(){
        val etPhotoUrl = EditText(this)
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.addArtist_dialogUrl_title)
            .setPositiveButton(R.string.label_dialog_add) { _, _ ->
                setImageFromUrl(etPhotoUrl.text.toString().trim { it <= ' ' })
            }
            .setNegativeButton(R.string.label_dialog_cancel, null)
        builder.setView(etPhotoUrl)
        builder.show()
    }

    private fun showDeletePhotoDialog(){
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.detalle_dialogDelete_title)
            .setMessage(String.format(Locale.ROOT,
                getString(R.string.detalle_dialogDelete_message)," este artista"))
            .setPositiveButton(R.string.label_dialog_delete) { _, _ ->
                setImageFromUrl(null)
            }
            .setNegativeButton(R.string.label_dialog_cancel, null)
        builder.show()
    }

    private fun showGallery(){
        openGalleryLauncher.launch("image/*")
    }

    private fun setImageFromUrl(url:String?){
        if(url!=null){
            Glide
                .with(this)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_sentiment_satisfied)
                .into(binding.imvPhoto)
        }else{
            binding.imvPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_size_select_actual))
        }
        lastPhotoUrl = url
    }
}
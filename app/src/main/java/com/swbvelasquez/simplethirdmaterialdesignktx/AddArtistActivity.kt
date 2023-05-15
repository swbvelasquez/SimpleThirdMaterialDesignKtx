package com.swbvelasquez.simplethirdmaterialdesignktx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.swbvelasquez.simplethirdmaterialdesignktx.databinding.ActivityAddArtistBinding
import com.swbvelasquez.simplethirdmaterialdesignktx.databinding.ActivityMainBinding

class AddArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddArtistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
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

    private fun setupToolbar() {
        setSupportActionBar(binding.tbMain)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun saveArtist(){

    }
}
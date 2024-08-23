package com.example.hiddengems

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

private lateinit var gemsAdapter: GemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gemsAdapter = GemsAdapter(mutableListOf())

        val rvGems: RecyclerView by lazy { findViewById(R.id.rvGems) }
        val tvSaveBtn: TextView by lazy { findViewById(R.id.tvSaveBtn) }
        val etName: EditText by lazy { findViewById(R.id.etName) }
        val etDesc: EditText by lazy { findViewById(R.id.etDesc) }
        val etAddress: EditText by lazy { findViewById(R.id.etAddress) }
        val etRating: EditText by lazy { findViewById(R.id.etRating) }
        val etCity: EditText by lazy { findViewById(R.id.etCity) }
        val etType: EditText by lazy { findViewById(R.id.etType) }




        rvGems.adapter = gemsAdapter
        rvGems.layoutManager = LinearLayoutManager(this)

        tvSaveBtn.setOnClickListener{
            val gemName = etName.text.toString()
            val gemDesc = etDesc.text.toString()
            val gemAddress = etAddress.text.toString()
            val gemRating = etRating.text.toString()
            val gemCity = etCity.text.toString()
            val gemType = etType.text.toString()
            val gem = Gem(gemName,gemDesc,gemAddress,gemCity,gemType,gemRating.toInt())
            gemsAdapter.addGem(gem)
        }


    }
}
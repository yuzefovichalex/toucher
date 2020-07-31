package com.alexyuzefovich.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alexyuzefovich.sample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSampleList()
        text.setOnClickListener {
            com.alexyuzefovich.toucher.PopupView(this).show(text)
            Handler().postDelayed({
                text.y = 500f
                /*text.animate()
                    .y(500f)*/
            }, 500)
        }
    }

    private fun initSampleList() {
        with(binding) {
            sampleList.adapter = SampleAdapter().also { it.submitList(generateListData()) }
            com.alexyuzefovich.toucher.Toucher().attachToRecyclerView(sampleList)
        }
    }

    private fun generateListData(): List<SampleEntity> {
        val listData = arrayListOf<SampleEntity>()
        for (i in 0 until 30) {
            val entity = SampleEntity(i.toLong(), "Item $i")
            listData.add(entity)
        }
        return listData
    }

}
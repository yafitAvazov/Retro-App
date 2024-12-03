package com.example.project1

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private val selectedShirtSizes = mutableListOf<String>()
    private val selectedShoeSizes = mutableListOf<Int>()
    private var totalSum = 0
    val selectedItems = mutableListOf<ImageItem>()

    @SuppressLint("ResourceAsColor", "MissingInflatedId", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gridView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val filterButton = findViewById<ImageView>(R.id.filter)
        val seekBar: SeekBar = findViewById(R.id.priceSeekBar)
        val minPriceText: TextView = findViewById(R.id.minPrice)
        val maxPriceText: TextView = findViewById(R.id.maxPrice)
        val applyButton = findViewById<Button>(R.id.apply_button)
        val totalSumTextView = findViewById<TextView>(R.id.total_sum)
        val gridView: GridView = findViewById(R.id.gridView)

        filterButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        val imageItems = listOf(
            ImageItem(R.drawable.item1, 50, shirtSizes = listOf("S", "M", "L"), shoeSizes = listOf(36, 37, 38)),
            ImageItem(R.drawable.item2, 40, shirtSizes = listOf("S", "M"), shoeSizes = listOf(36, 37)),
            ImageItem(R.drawable.item3, 70, shirtSizes = listOf("M", "L"), shoeSizes = listOf(37, 38, 39)),
            ImageItem(R.drawable.item5, 100, shirtSizes = listOf("S", "M"), shoeSizes = listOf(36, 37, 40)),
            ImageItem(R.drawable.item6, 80, shirtSizes = listOf("L"), shoeSizes = listOf(38, 39)),
            ImageItem(R.drawable.item7, 30, shirtSizes = listOf("S", "L"), shoeSizes = listOf(36, 40)),
            ImageItem(R.drawable.item8, 90, shirtSizes = listOf("M", "L"), shoeSizes = listOf(37, 38, 39)),
            ImageItem(R.drawable.item9, 20, shirtSizes = listOf("S"), shoeSizes = listOf(36, 37)),
            ImageItem(R.drawable.item10, 110, shirtSizes = listOf("M", "L"), shoeSizes = listOf(38, 40)),
            ImageItem(R.drawable.item11, 45, shirtSizes = listOf("S", "M"), shoeSizes = listOf(36, 37, 38)),
            ImageItem(R.drawable.item12, 65, shirtSizes = listOf("L"), shoeSizes = listOf(39, 40)),
            ImageItem(R.drawable.item13, 55, shirtSizes = listOf("S", "M", "L"), shoeSizes = listOf(36, 37, 38, 40)),
            ImageItem(R.drawable.item14, 75, shirtSizes = listOf("M"), shoeSizes = listOf(36, 37)),
            ImageItem(R.drawable.item15, 85, shirtSizes = listOf("S", "L"), shoeSizes = listOf(39, 40)),
            ImageItem(R.drawable.item16, 95, shirtSizes = listOf("M", "L"), shoeSizes = listOf(37, 38)),
            ImageItem(R.drawable.item17, 25, shirtSizes = listOf("S", "M"), shoeSizes = listOf(36, 37, 38))
        )

        val adapter = ImageAdapter(this, imageItems, { itemPriceChange ->
            totalSum += itemPriceChange
                totalSumTextView?.text =  getString(R.string.total_sum) + " $${totalSum}"
        }, { selectedItem ->
            if (selectedItem.isSelected) {
                selectedItems.add(selectedItem)
            } else {
                selectedItems.remove(selectedItem)
            }
        })
        gridView.adapter = adapter


        // Checkbox למידות חולצה
        val shirtCheckBoxes = listOf(
            findViewById<CheckBox>(R.id.checkbox_s),
            findViewById<CheckBox>(R.id.checkbox_m),
            findViewById<CheckBox>(R.id.checkbox_l)
        )

        // Checkbox למידות נעליים
        val shoeCheckBoxes = listOf(
            findViewById<CheckBox>(R.id.checkbox_36),
            findViewById<CheckBox>(R.id.checkbox_37),
            findViewById<CheckBox>(R.id.checkbox_38),
            findViewById<CheckBox>(R.id.checkbox_39),
            findViewById<CheckBox>(R.id.checkbox_40)
        )

        // סימון מידות חולצה
        shirtCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedShirtSizes.add(checkBox.text.toString())
                } else {
                    selectedShirtSizes.remove(checkBox.text.toString())
                }
            }
        }

        // סימון מידות נעליים
        shoeCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedShoeSizes.add(checkBox.text.toString().toInt())
                } else {
                    selectedShoeSizes.remove(checkBox.text.toString().toInt())
                }
            }
        }


        // טווח מחירים
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minPriceText.text = "$0"
                maxPriceText.text = "$$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val filteredItems = imageItems.filter { item ->
                    val price = item.price
                    price in 0..(seekBar?.progress ?: 500)
                }
            }
        })

            //סינון הפריטים לפי הטווח שנבחר
        applyButton.setOnClickListener {
            val maxPrice = seekBar.progress
            val filteredItems = imageItems.filter { item ->
                val price = item.price
                val matchesPrice = price in 0..maxPrice
                val matchesShirtSize = selectedShirtSizes.isEmpty() || item.shirtSizes.any { it in selectedShirtSizes }
                val matchesShoeSize = selectedShoeSizes.isEmpty() || item.shoeSizes.any { it in selectedShoeSizes }
                matchesPrice && matchesShirtSize && matchesShoeSize
            }
            adapter.updateList(filteredItems)
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        filterButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }




        val themeSwitch: Switch = findViewById(R.id.theme_switch)
        val secondIcon: ImageView = findViewById(R.id.light_icon)
        val layout: LinearLayout = findViewById(R.id.layout)

        //התצוגה לאחר לחיצה על אייקון עגלה
        findViewById<ImageView>(R.id.shopping_icon).setOnClickListener {
            val b = BottomSheetDialog(this)
            b.setContentView(R.layout.buy_button_dialog)

            val bottomTotalSumTextView = b.findViewById<TextView>(R.id.total_sum)
            val linearLayout = b.findViewById<LinearLayout>(R.id.linearLayout1)
            Log.d("MainActivity", "BottomSheet totalSum: $totalSum")
            if (bottomTotalSumTextView != null) {
                bottomTotalSumTextView.text = getString(R.string.total_sum) + " $${totalSum}"
                linearLayout?.removeAllViews()

                selectedItems.forEach { item ->
                    val itemView = LayoutInflater.from(this).inflate(R.layout.item_summary, linearLayout, false)

                    val itemPriceTextView = itemView.findViewById<TextView>(R.id.itemPrice)
                    val itemShirtSizeTextView = itemView.findViewById<TextView>(R.id.itemShirtSize)
                    val itemShoeSizeTextView = itemView.findViewById<TextView>(R.id.itemShoeSize)
                    val itemImageView = itemView.findViewById<ImageView>(R.id.itemImage)

                    itemPriceTextView.text = getString(R.string.price) + " $${item.price}"
                    itemShirtSizeTextView.text = getString(R.string.title_clothes_size) + " ${item.selectedShirtSize ?: getString(R.string.none)}"
                    itemShoeSizeTextView.text = getString(R.string.title_shoes_sizes) + " ${item.selectedShoeSize ?: getString(R.string.none)}"
                    itemImageView.setImageResource(item.imageResId)
                    linearLayout?.addView(itemView)
                }
            } else {
                Toast.makeText(this, "Error: Unable to display total sum", Toast.LENGTH_SHORT).show()
            }
            val btn = b.findViewById<Button>(R.id.get_tickets_btn)
            btn?.setOnClickListener {
                val alphaAnim = android.view.animation.AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
                btn.startAnimation(alphaAnim)
                Toast.makeText(this, getString(R.string.total_purchase) , Toast.LENGTH_SHORT).show()
                selectedItems.forEach { item ->
                    item.isSelected = false
                    item.selectedShirtSize = null
                    item.selectedShoeSize = null
                }
                selectedItems.clear()
                linearLayout?.removeAllViews()
                (gridView.adapter as ImageAdapter).notifyDataSetChanged()
                linearLayout?.removeAllViews()
                totalSum = 0
                bottomTotalSumTextView?.text = getString(R.string.total_sum) + " $${totalSum}"
            }
            b.show()
        }




        //  פונקציה למתג התאורה
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                secondIcon.setImageResource(R.drawable.baseline_dark_mode_24)
                layout.setBackgroundColor(Color.BLACK)
                themeSwitch.thumbTintList = ContextCompat.getColorStateList(this, R.color.white)
                themeSwitch.trackTintList = ContextCompat.getColorStateList(this, R.color.black)
                minPriceText.setTextColor(Color.WHITE)
                maxPriceText.setTextColor(Color.WHITE)
                seekBar.thumbTintList=ContextCompat.getColorStateList(this, R.color.white)
            } else {
                secondIcon.setImageResource(R.drawable.baseline_light_mode_24)
                layout.setBackgroundColor(Color.WHITE)
                minPriceText.setTextColor(Color.BLACK)
                maxPriceText.setTextColor(Color.BLACK)
                seekBar.thumbTintList=ContextCompat.getColorStateList(this, R.color.gray)

            }
        }

    }}
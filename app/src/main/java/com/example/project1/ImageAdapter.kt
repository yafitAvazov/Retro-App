package com.example.project1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat


data class ImageItem(
    val imageResId: Int,
    val price: Int,
    val shirtSizes: List<String> = listOf(),
    val shoeSizes: List<Int> = listOf(),
    var isSelected: Boolean = false,
    var selectedShirtSize: String? = null,
    var selectedShoeSize: Int? = null
)
class ImageAdapter(
    private val context: Context,
    private var imageItems: List<ImageItem>,
    private val onItemAdded: (Int) -> Unit,
    private val onItemSelected: (ImageItem) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = imageItems.size

    override fun getItem(position: Int): Any = imageItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val priceTextView = view.findViewById<TextView>(R.id.title)
        val shirtSizesGroup = view.findViewById<RadioGroup>(R.id.radio_group_clothes_size)
        val shoeSizesGroup = view.findViewById<RadioGroup>(R.id.radio_group_shoes_size)
        val addButton = view.findViewById<ImageView>(R.id.second_icon)
        val deleteButton = view.findViewById<ImageView>(R.id.first_icon)
        val cardView = view.findViewById<CardView>(R.id.card_view)

        val item = imageItems[position]
        imageView.setImageResource(item.imageResId)
        priceTextView.text = context.getString(R.string.price) + " $${item.price}"



        // יצירת RadioButtons עבור מידות חולצה
        shirtSizesGroup.removeAllViews()
        item.shirtSizes.forEach { size ->
            val radioButton = RadioButton(context)
            radioButton.text = size
            shirtSizesGroup.addView(radioButton)

        if (size == item.selectedShirtSize) {
            radioButton.isChecked = true
        }
            radioButton.setOnClickListener {
                item.selectedShirtSize = size
            }
        }



        // יצירת RadioButtons עבור מידות נעליים
        shoeSizesGroup.removeAllViews()
        item.shoeSizes.forEach { size ->
            val radioButton = RadioButton(context)
            radioButton.text = size.toString()
            shoeSizesGroup.addView(radioButton)

        if (size == item.selectedShoeSize) {
            radioButton.isChecked = true
        }
        radioButton.setOnClickListener {
            item.selectedShoeSize = size
        }
    }



        //פעולות עבור כפתור הוסף
        addButton.setOnClickListener {
            if (!item.isSelected) {
                item.isSelected = true
                val itemPrice = item.price
                if (itemPrice > 0) {
                    (context as? MainActivity)?.selectedItems?.add(item)
                    onItemAdded(itemPrice)
                } else {
                    Log.e("ImageAdapter", "Error: Invalid price $itemPrice")
                }
                notifyDataSetChanged()
            }
        }


        // שינוי צבע רקע כרטיס
        if (item.isSelected) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_gray))
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }


            //פעולות עבור כפתור מחיקה
        deleteButton.setOnClickListener {
            if (item.isSelected) {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
                val dialog = AlertDialog.Builder(context, R.style.CustomDialogTheme)
                    .setView(dialogView)
                    .create()

                dialogView.findViewById<Button>(R.id.confirm_button).setOnClickListener {
                    item.isSelected = false
                    (context as? MainActivity)?.selectedItems?.remove(item)
                    onItemAdded(-item.price)
                    notifyDataSetChanged()
                    dialog.dismiss()
                }
                dialogView.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        return view
    }


    //  עדכון הרשימה
    fun updateList(newItems: List<ImageItem>) {
        imageItems = newItems
        notifyDataSetChanged()
    }
}
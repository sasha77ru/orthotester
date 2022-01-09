package ru.sasha77.orthotester

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.View
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "otDB.db"
const val DATABASE_VERSION = 1

lateinit var measTimer : MA.MeasTimer
val theTable = mutableListOf<MA.MyRow>()
lateinit var mainListAdapter : BaseAdapter
var firstStart : Boolean = true

fun BaseAdapter.update(sq : Sqllo) {
//    cursor.requery()
    sq.inflateTheTableFromDatabase()
    notifyDataSetChanged()
}

fun vlog(s : String, red: Boolean = false) {
    when (red) {
        false -> Log.d("peps",s)
        true -> Log.e("peps",s)
    }
}

@SuppressLint("SimpleDateFormat")
fun onlyDate () : Date = SimpleDateFormat("yyyy-MM-dd").parse(SimpleDateFormat("yyyy-MM-dd").format(Date())) ?: throw NullPointerException()

fun probeColor (result : Float) : Int {
    return when {
        result < 0                  -> R.color.worstColor
        result >= 0 && result < 5   -> R.color.badColor
        result >= 5 && result < 10  -> R.color.goodColor
        result >= 10                -> R.color.excellentColor
        else -> R.color.errorColor
    }
}

fun mySetDrawableColor (view:View, color:Int) {
    when (val background = view.background) {
        is ShapeDrawable -> background.paint.color = color
        is GradientDrawable -> background.setColor(color)
        is ColorDrawable -> background.color = color
    }
}


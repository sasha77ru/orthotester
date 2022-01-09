@file:Suppress("DEPRECATION", "UNUSED_ANONYMOUS_PARAMETER")

package ru.sasha77.orthotester

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.*

import kotlinx.android.synthetic.main.activity_ma.*
import kotlinx.android.synthetic.main.content_m.*
import java.lang.IllegalArgumentException
import android.media.RingtoneManager
import android.view.inputmethod.InputMethodManager
import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import android.view.*
import android.view.animation.AnimationUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MA : AppCompatActivity(), View.OnClickListener {

    lateinit var sq : Sqllo
    lateinit var timerView: TextView
    lateinit var editPulse1: EditText
    lateinit var editPulse2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ma)
        setSupportActionBar(toolbar)
        fab.setOnClickListener(this@MA)

        measTimer = MeasTimer(resources.getInteger(R.integer.measTimerPeriod))
    }

    override fun onStart() {
        super.onStart()
        sq = Sqllo(this@MA,DB_NAME)
        sq.inflateTheTableFromDatabase()

        //<editor-fold desc="old adapter">
        //        mainListAdapter = object : SimpleCursorAdapter(
//            this@MA,
//            R.layout.meas_la,
//            cursor,
//            arrayOf("date","pulse1","pulse2","probe"),
//            arrayOf(R.id.textMeasDate,R.id.textMeasPulse1,R.id.textMeasPulse2,R.id.textMeasProbe).toIntArray(),
//            0
//        ) {
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//                val v = super.getView(position, convertView, parent)
////                val dateField = v.findViewById<TextView>(R.id.textMeasDate)
////                dateField.text = DateFormat.getDateInstance().format(SimpleDateFormat("yyyy-MM-dd").parse(dateField.text.toString()))
//                v.findViewById<TextView>(R.id.textMeasProbe).setBackgroundColor(resources.getColor(probeColor(getCursor()
//                    .getFloat(getCursor().getColumnIndex("probe")))))
//                v.tag = getCursor().getInt(getCursor().getColumnIndex("_id"))
//                v.setOnClickListener {
//                    v.setBackgroundColor(resources.getColor(R.color.raw2DeleteColor))
//                    showRemoveMeasDialog(v)
//                }
//                return v
//            }
//
//        }
        //</editor-fold>

        mainListAdapter = object : ArrayAdapter<MyRow>(this,R.layout.meas_la, theTable
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val v = convertView ?: LayoutInflater.from(this@MA).inflate(R.layout.meas_la,null)
//                val dateField = v.findViewById<TextView>(R.id.textMeasDate)
//                dateField.text = DateFormat.getDateInstance().format(SimpleDateFormat("yyyy-MM-dd").parse(dateField.text.toString()))

                val probe = getItem(position).probe
                v.findViewById<TextView>(R.id.textMeasDate).text = getItem(position).date
                if (probe>-100F) {
                    v.findViewById<TextView>(R.id.textMeasPulse1).text = getItem(position).pulse1.toString()
                    v.findViewById<TextView>(R.id.textMeasPulse2).text = getItem(position).pulse2.toString()
                    v.findViewById<TextView>(R.id.textMeasProbe).text = probe.toString()
                    v.findViewById<TextView>(R.id.textMeasProbe).
                        setBackgroundColor(resources.getColor(probeColor(getItem(position).probe)))
                    v.setOnClickListener {
                        v.setBackgroundColor(resources.getColor(R.color.raw2DeleteColor))
                        showRemoveMeasDialog(v)
                    }
                }
                v.tag = getItem(position)._id
                return v
            }
        }
        mainListView.adapter = mainListAdapter
        if (firstStart) showAddMeasDialog()
        firstStart = false
    }

    data class MyRow (val _id : Int, val date : String, val pulse1 : Float, val pulse2 : Float, val probe :Float)

    override fun onStop() {
        sq.close()
        super.onStop()
    }

    override fun onBackPressed() {
        firstStart = true
        super.onBackPressed()
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {showAddMeasDialog()}
            R.id.timerView -> measTimer.onClick()
        }
    }

    private fun showAddMeasDialog () {
        val la = LinearLayout(this@MA)
        this@MA.layoutInflater.inflate(R.layout.add_meas_dialog, la)
        editPulse1 = la.findViewById(R.id.editPulse1)
        editPulse2 = la.findViewById(R.id.editPulse2)
        val computeButton = la.findViewById<TextView>(R.id.computeButton)
        timerView = la.findViewById(R.id.timerView)
        val textResult = la.findViewById<TextView>(R.id.textResult)
        editPulse1.transformationMethod = null
        editPulse2.transformationMethod = null
        val ad = AlertDialog.Builder(this@MA)
            .setView(la)
            .show()
        editPulse1.addTextChangedListener(MyTextWatcher(editPulse1) {
            editPulse2.requestFocus()
        })
        editPulse2.addTextChangedListener(MyTextWatcher(editPulse2) {
            computeButton.performClick()
        })
        computeButton.setOnClickListener {
            try {
                val pulse1 = try {
                    editPulse1.text.toString().toFloat()
                } catch (e: Exception) {
                    throw IllegalArgumentException("pulse_wrong")
                }
                val pulse2 = try {
                    editPulse2.text.toString().toFloat()
                } catch (e: Exception) {
                    throw IllegalArgumentException("delta_wrong")
                }
                val result = compute(pulse1,pulse2)
                textResult.setBackgroundColor(resources.getColor(probeColor(result)))
                sq.addMeas(pulse1,pulse2,result)

                mainListView.smoothScrollToPosition(0)
                //Wait for KB Hide
                Handler(this@MA.mainLooper).postDelayed({runOnUiThread {
                    floaterText.text = result.toString()
                    mySetDrawableColor(floater,resources.getColor(probeColor(result)))
                    floater.visibility = View.VISIBLE
                    floater.startAnimation(AnimationUtils.loadAnimation(this,R.anim.result2corner))
                    //Wait for animation stop
                    Handler(this@MA.mainLooper).postDelayed({runOnUiThread {
                        floater.visibility = View.INVISIBLE
                        mainListAdapter.update(sq)
                    }},resources.getInteger(R.integer.resultAnimation_overallTime).toLong())
                }},resources.getInteger(R.integer.wait4HideKBTime).toLong())

                ad.dismiss()
            } catch (e: Exception) {
                val message = e.message ?: ""
                with (when {
                    message.matches("pulse.*".toRegex()) -> editPulse1
                    message.matches("delta.*".toRegex()) -> editPulse2
                    else -> null
                }) {
                    this?.setBackgroundColor(this@MA.resources.getColor(R.color.errorColor))
                    this?.requestFocus()
                    Handler().postDelayed({
                        this?.setBackgroundColor(this@MA.resources.getColor(android.R.color.transparent))
                    },1000)
                }
                textResult.setBackgroundColor(this@MA.resources.getColor(R.color.errorColor))
                val id = resources.getIdentifier(e.message,"string",packageName)
                vlog("UserErr: ${e.message}",red = true)
                textResult.text = (if (id > 0) resources.getString(id) else getString(R.string.incredibleError)) ?: getString(R.string.incredibleError)
            }
        }
        timerView.setOnClickListener(this@MA)
    }
    fun showRemoveMeasDialog (v: View) {
        var ad : AlertDialog? = null
        ad = AlertDialog.Builder(this@MA)
            .setMessage(R.string.confirmRemoving)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog, which ->
                /**
                 * This method will be invoked when a button in the dialog is clicked.
                 *
                 * @param dialog the dialog that received the click
                 * @param which the button that was clicked (ex.
                 * [DialogInterface.BUTTON_POSITIVE]) or the position
                 * of the item clicked
                 */
                v.setBackgroundColor(resources.getColor(R.color.measBGColor))
                sq.removeMeas(v.tag as Int)
                mainListAdapter.update(sq)
            }
            .setNegativeButton(R.string.cancel) { dialog, which ->
                ad?.cancel()
            }
            .setOnCancelListener {
                v.setBackgroundColor(resources.getColor(R.color.measBGColor))
            }
            .show()
    }
    private fun showAboutDialog () {
        AlertDialog.Builder(this@MA)
            .setTitle("${getString(R.string.app_name)} v.${getString(R.string.ver)}")
            .setMessage(R.string.contentOfAbout)
            .setPositiveButton(R.string.ok,null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_m, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_about -> {showAboutDialog();true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class MeasTimer (private val theTime: Int) {
        private var elapsed : Int = -1
        private var timer : Timer? = null
        fun onClick () {
            if (timer == null) start() else stop()
        }
        private fun start () {
            with (if (editPulse1.text.toString()=="") editPulse1 else editPulse2) {
                requestFocus()
                myShowKeyb(this)
            }
            print(getString(R.string.prepareTimer))
            timer = Timer().apply {
                    scheduleAtFixedRate(resources.getInteger(R.integer.measTimerBeReadyDelay).toLong(),1000) {
                    if (elapsed < 0) {
                        elapsed = theTime
                        sound()
                        print()
                    } else {
                        elapsed--
                        print()
                        if (elapsed == 0) {
                            sound()
                            elapsed = -1
                            stop()
                        }
                    }
                }
            }
        }
        private fun stop () {
            elapsed = -1
            print(getString(R.string.startTimer))
            timer?.cancel()
            timer = null
        }
        private fun print (s : String? = null) {
            runOnUiThread {timerView.text = s ?: elapsed.toString()}
        }

        private fun sound() {
            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, notification)
                r.play()
            } catch (e: Exception) {

            }
        }
    }

    @Suppress("unused")
    fun myHideKeyb() {
        runOnUiThread {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    maView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        }
    }

    fun myShowKeyb(v: View) {
        runOnUiThread {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private inner class MyTextWatcher (val et: EditText, val onEnter : () -> Unit) : TextWatcher {
        var sBefore: String = et.text.toString()

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length > start && s[start] == '\n') {
                et.clearFocus()
                et.setText(sBefore)
                onEnter()
            } else if (!s.matches("\\d*".toRegex())) {
                et.setText(sBefore)
                et.setSelection(start)
            } else if (s.length > 3) {
                //<editor-fold desc="Confine string length">
                et.setText(s.subSequence(0,3))
                et.setSelection(if (start < 3) start+1 else 3)
                //</editor-fold>
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            this.sBefore = s.toString()
        }

        override fun afterTextChanged(s: Editable) {}

    }
}




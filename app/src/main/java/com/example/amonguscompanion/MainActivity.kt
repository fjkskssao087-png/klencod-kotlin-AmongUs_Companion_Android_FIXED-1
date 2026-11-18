package com.example.amonguscompanion

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import android.text.InputType

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 10, 18)
    private val panel = Color.rgb(20, 23, 35)
    private val red = Color.rgb(255, 45, 65)
    private val cyan = Color.rgb(45, 205, 255)
    private lateinit var content: LinearLayout
    private val events = mutableListOf<String>()
    private var crewCount = 10
    private var impostors = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun base(title: String) {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 22, 18, 24)
        }
        scroll.addView(page)
        setContentView(scroll)
        page.addView(label("AMONG US  •  COMPANION", 12, cyan))
        page.addView(label(title, 27, Color.WHITE))
        page.addView(label("أداة مساعدة مستقلة — لا تتصل باللعبة ولا تقرأ مواقع اللاعبين مباشرة.", 13, Color.LTGRAY))
        val nav = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("الرئيسية", "المحلل", "السجل").forEachIndexed { i, name ->
            val b = Button(this).apply { text = name; setOnClickListener { when(i) { 0 -> showHome(); 1 -> showAnalyzer(); else -> showLog() } } }
            nav.addView(b, LinearLayout.LayoutParams(0, -2, 1f))
        }
        page.addView(nav)
        content = page
    }

    private fun label(text: String, size: Int, color: Int): TextView =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(color)
            setPadding(4, 12, 4, 12)
        }

    private fun card(title: String, detail: String) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(14, 8, 14, 8)
            setBackgroundColor(panel)
        }
        box.addView(label(title, 18, red))
        box.addView(label(detail, 14, Color.WHITE))
        content.addView(box, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 10, 0, 2) })
    }

    private fun action(text: String, fn: () -> Unit) {
        val b = Button(this).apply { this.text = text; setOnClickListener { fn() } }
        content.addView(b, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 6, 0, 2) })
    }

    private fun showHome() {
        base("لوحة التحكم")
        card("الحالة", "جاهز للعمل دون اتصال • لا يحتاج صلاحيات خاصة")
        card("الرادار", "يعرض مخططاً إرشادياً للخريطة فقط. إحداثيات اللاعبين الحقيقية غير متاحة للتطبيق المستقل.")
        card("أدوات الجلسة", "تسجيل الأحداث يدوياً، حساب احتمالات تقريبية، ومراجعة المهام والملاحظات.")
        action("فتح محلل المشتبه بهم") { showAnalyzer() }
        action("تسجيل حدث جديد") { addEventDialog() }
        action("عرض سجل الجلسة") { showLog() }
        action("دليل الاستخدام") {
            android.app.AlertDialog.Builder(this).setTitle("طريقة الاستخدام")
                .setMessage("1) أدخل عدد اللاعبين والمحتالين.\n2) سجّل ما شاهدته بنفسك أثناء الجولة.\n3) استخدم النتيجة كمؤشر احتمالي فقط، وليست كشفاً مؤكداً.\n\nلا يوجد حقن داخل اللعبة أو تجاوز لمكافحة الغش.")
                .setPositiveButton("حسناً", null).show()
        }
    }

    private fun showAnalyzer() {
        base("محلل الاشتباه")
        val crew = EditText(this).apply {
            hint = "عدد اللاعبين (4–15)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(crewCount.toString())
        }
        val imp = EditText(this).apply {
            hint = "عدد المحتالين (1–3)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(impostors.toString())
        }
        content.addView(crew)
        content.addView(imp)
        val result = label("أدخل الأعداد لحساب الاحتمال الأساسي.", 16, cyan)
        content.addView(result)
        action("احسب الاحتمال") {
            val c = crew.text.toString().toIntOrNull() ?: 10
            val m = imp.text.toString().toIntOrNull() ?: 2
            if (c !in 4..15 || m !in 1..3 || m >= c) {
                result.text = "القيم غير صحيحة. اللاعبين 4–15 والمحتالون 1–3."
            } else {
                crewCount = c; impostors = m
                val p = (m.toDouble() / c * 100)
                result.text = "الاحتمال الأساسي العشوائي: %.1f%% لكل لاعب قبل ملاحظة أي سلوك.".format(p)
            }
        }
        card("مهم", "هذا حساب احتمالي ابتدائي، وليس تحديداً لهوية المحتال. لا يمكن استنتاج مواقع أو أدوار مخفية دون بيانات موثوقة.")
        action("إضافة ملاحظة عن لاعب") { addEventDialog() }
    }

    private fun addEventDialog() {
        val input = EditText(this).apply { hint = "مثال: شاهدت الأحمر قرب الكهرباء" }
        android.app.AlertDialog.Builder(this).setTitle("تسجيل ملاحظة")
            .setView(input)
            .setPositiveButton("حفظ") { _, _ ->
                val value = input.text.toString().trim()
                if (value.isNotEmpty()) {
                    events.add(0, value)
                    Toast.makeText(this, "تم حفظ الملاحظة", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("إلغاء", null).show()
    }

    private fun showLog() {
        base("سجل الجلسة")
        if (events.isEmpty()) card("لا توجد أحداث", "استخدم زر تسجيل حدث لإضافة ملاحظاتك.")
        else events.forEachIndexed { i, event -> card("ملاحظة ${i + 1}", event) }
        action("مسح السجل") {
            events.clear()
            showLog()
        }
    }
}
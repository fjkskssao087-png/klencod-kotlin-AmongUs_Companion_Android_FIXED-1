Among Us Companion — مشروع Android

إصلاح البناء:
- تم ضبط Java sourceCompatibility وtargetCompatibility على 17.
- تم ضبط Kotlin jvmTarget على 17 لمطابقة إعدادات Java في GitHub Actions / Klencod IDE.

المميزات:
- لوحة تحكم عربية داكنة.
- محلل احتمال أساسي حسب عدد اللاعبين والمحتالين.
- سجل ملاحظات محلي للجلسة.
- يعمل دون اتصال ولا يطلب صلاحيات حساسة.

حدود التشغيل:
- لا يكشف مواقع اللاعبين الحقيقية ولا يقرأ بيانات اللعبة.
- لا يحتوي على حقن أو تعديل للعبة أو تجاوز لمكافحة الغش.
- لا توجد طريقة مضمونة لمنع الحظر عند استخدام أدوات غش؛ هذا المشروع لا يتصل باللعبة ولا يعدّلها.

البناء:
1. فك ضغط ZIP.
2. افتح المجلد الذي يحتوي settings.gradle.kts في Klencod IDE.
3. نفّذ Gradle Sync.
4. شغّل مهمة :app:assembleDebug.

الإصدارات:
- Android Gradle Plugin 8.6.1
- Kotlin 2.0.20
- Gradle Wrapper 8.14
- JDK 17
- compileSdk 35 / minSdk 23

تم إصلاح سبب الخطأ الظاهر في سجل البناء: اختلاف JVM target بين Java (1.8) وKotlin (17).
لم يتم تنفيذ بناء فعلي في هذه الجلسة؛ نتيجة Klencod الجديدة هي التي تؤكد نجاح APK.





package com.example.tide2a


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.tide2a.databinding.ActivityMainBinding
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.*
import kotlin.random.Random


//import kotlinx.android deprecated using view binding instead

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: com.example.tide2a.databinding.ActivityMainBinding

    var ports = arrayOf("StHelier","StMalo","StMarys","Brehat","Liverpool","Plymouth","Cherbourg","Roscoff","StHFr")
    lateinit var portChosen:String
    val wisdom:List<String> = listOf("Don't just do your best; make your best a little better every day",
"Green fingers should always be crossed", "Time and Tide wait for no man","The Sea doesn't return your respect",
"This life is a just a quantum sketch of reality (probably)","Mankind is like a cancer on the world", "Protect the planet for our offspring",
"Your life is yours, and your only chance to do right", "It is more important to do the right thing than to do the thing right",
"It is more important to get the right thing done than to get the thing done right", "Waste not , want not",
"'There IS a God, because He made this hole to fit me perfectly' said the puddle","Eat something you don't like every day",
"If in doubt, Do It!")
    val degToRad = kotlin.math.PI/180
    var nooow: ZonedDateTime = ZonedDateTime.now(Clock.systemUTC())
    val decFormat = "%.2f"//this just forces the 2nd decimal place. set to 1 if desired. 3=n/a
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var daysAdded:Long=0
        if (binding.etDaysAdvance.text.toString().trim() != "")
        {daysAdded = binding.etDaysAdvance.text.toString().toLong()}
        else    {daysAdded = 0}

        var noow: ZonedDateTime = nooow.plusDays(daysAdded)
        var noowSecs:Long  = noow.toEpochSecond()
        var noowMins:Long  = (noowSecs)/60
        //var hourOfDay:String = noow.format(DateTimeFormatter.ofPattern("HH"))//00 to23
        var yearString:String = noow.format(DateTimeFormatter.ofPattern("yyyy"))
        var yearIndix:Int=yearString.toInt() - 2020
        val yearStart = ZonedDateTime.of(yearString.toInt(),1,1,0,0,0,0, ZoneOffset.UTC)
        val yearStartSecs:Long = yearStart.toEpochSecond()
        val yearStartMins:Long = yearStartSecs/60
        val minsIn = noowMins - yearStartMins
        //val hoursIn = minsIn/60// can be used to calculate movements of harmonic since year start
        val daysIn = noow.format(DateTimeFormatter.ofPattern("D"))
        //val dayStartMins = yearStartMins + daysIn.toLong()*24*60//needed for hourly tides


        val spinner1:Spinner = binding.portSpinner
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, ports)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.setAdapter (aa)
        spinner1.onItemSelectedListener = this

  //      val harmonicsList= listOf("M2","S2","N2","K2","M4","MS4","NU2","MU2","L2","K1","tN2","O1","T2","MN4","SA","P1","Q1")

        var m2EqArg =  0F;var m2Node = 0F;var s2EqArg  =  0F;var s2Node = 1.0F
        var n2EqArg =  0F;var n2Node = 0F;var k2EqArg  =  0F;var k2Node = 0F
        var m4EqArg =  0F;var m4Node = 0F;var ms4EqArg =  0F; var ms4Node = 0F
        var nu2EqArg = 0F;var nu2Node= 0F;var mu2EqArg =  0F;var mu2Node = 0F
        var l2EqArg =  0F;var l2Node = 0F;var k1EqArg  =  0F;var k1Node = 0F
        var tN2EqArg = 0F;var tN2Node= 0F;var o1EqArg  =  0F;var o1Node = 0F
        var t2EqArg =  0F;var t2Node = 1.0F;var mn4EqArg  =  0F;var mn4Node = 0F
        var saEqArg =  0F;var saNode = 0F;var p1EqArg  =  0F;var p1Node = 1.0F
        var q1EqArg  =  0F;var q1Node = 1.0F
        //BEFORE YOU START ON ALL THIS CHECK IF YEAR HAS CHANGED...ALSO AT END OF ALL THIS STORE THE YEAR IN SHARED PREFERENCES
        var pickList:List<String> =applicationContext.assets.open("M2eqargs.txt").bufferedReader().readLines()
        m2EqArg = pickList[yearIndix].toFloat()
        //bufferedReader.close()
        pickList =applicationContext.assets.open("M2nodes.txt").bufferedReader().readLines()
        m2Node = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("N2eqargs.txt").bufferedReader().readLines()
        n2EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("N2nodes.txt").bufferedReader().readLines()
        n2Node = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("K2eqargs.txt").bufferedReader().readLines()
        k2EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("K2nodes.txt").bufferedReader().readLines()
        k2Node = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("M4eqargs.txt").bufferedReader().readLines()
        m4EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("M4nodes.txt").bufferedReader().readLines()
        m4Node = pickList[yearIndix].toFloat()
        mn4Node = pickList[yearIndix].toFloat()

        pickList = applicationContext.assets.open("MS4eqargs.txt").bufferedReader().readLines()
        ms4EqArg = pickList[yearIndix].toFloat()

        pickList = applicationContext.assets.open("MS4MU2NU2nodes.txt").bufferedReader().readLines()
        ms4Node = pickList[yearIndix].toFloat()
        nu2Node = pickList[yearIndix].toFloat()
        mu2Node = pickList[yearIndix].toFloat()
        tN2Node = pickList[yearIndix].toFloat()


        pickList = applicationContext.assets.open("NU2eqargs.txt").bufferedReader().readLines()
        nu2EqArg = pickList[yearIndix].toFloat()

        pickList = applicationContext.assets.open("MU2eqargs.txt").bufferedReader().readLines()
        mu2EqArg = pickList[yearIndix].toFloat()

        pickList = applicationContext.assets.open("L2eqargs.txt").bufferedReader().readLines()
        l2EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("L2nodes.txt").bufferedReader().readLines()
        l2Node = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("K1eqargs.txt").bufferedReader().readLines()
        k1EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("K1nodes.txt").bufferedReader().readLines()
        k1Node = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("TN2eqargs.txt").bufferedReader().readLines()
        tN2EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("O1eqargs.txt").bufferedReader().readLines()
        o1EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("O1nodes.txt").bufferedReader().readLines()
        o1Node = pickList[yearIndix].toFloat()
        q1Node = pickList[yearIndix].toFloat()

        pickList = applicationContext.assets.open("T2eqargs.txt").bufferedReader().readLines()
        t2EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("MN4eqargs.txt").bufferedReader().readLines()
        mn4EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("T2eqargs.txt").bufferedReader().readLines()
        saEqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("MN4eqargs.txt").bufferedReader().readLines()
        p1EqArg = pickList[yearIndix].toFloat()
        pickList = applicationContext.assets.open("MN4eqargs.txt").bufferedReader().readLines()
        q1EqArg = pickList[yearIndix].toFloat()
var argNodeList = listOf<Float>(m2EqArg,m2Node,s2EqArg,s2Node,n2EqArg,n2Node,k2EqArg,k2Node,
        m4EqArg,m4Node,ms4EqArg,ms4Node,nu2EqArg,nu2Node,mu2EqArg,mu2Node,l2EqArg,l2Node,
        k1EqArg,k1Node,tN2EqArg,tN2Node,o1EqArg,o1Node,t2EqArg,t2Node,
        mn4EqArg,mn4Node,saEqArg,saNode,p1EqArg,p1Node,q1EqArg,q1Node)


        binding.btnRefresh.setOnClickListener()
        {

        calcTideNow(argNodeList)
        calcHwLw(argNodeList)
        this.binding.btnRefresh.closeKeyboard()
//just a bit of amusement:-
            val wisIndex = Random.nextInt(14)
            var wisWord = wisdom[wisIndex]
            binding.etTestBox.setText(wisWord)

            var daysAdded:Long=0//don't understand why i have to repeat this like line 45-50 (context?)
            if (binding.etDaysAdvance.text.toString().trim() != "")
            {daysAdded = binding.etDaysAdvance.text.toString().toLong()}
            else    {daysAdded = 0}

            var noow: ZonedDateTime = nooow.plusDays(daysAdded)
        var bDayCheck:String = noow.format(DateTimeFormatter.ofPattern("MM dd")).toString()
             if (bDayCheck == "05 13"){Toast.makeText(this,"HAPPY BIRTHDAY JOHN , LOVE FROM GRANDAD XX",Toast.LENGTH_LONG).show()}
        else if (bDayCheck == "04 18"){Toast.makeText(this,"HAPPY BIRTHDAY GEORGE , LOVE FROM DAD XX",Toast.LENGTH_LONG).show()}
        else if (bDayCheck == "08 02"){Toast.makeText(this,"HAPPY BIRTHDAY IRENE , LOVE FROM JOHN XX",Toast.LENGTH_LONG).show()}
        else if (bDayCheck == "08 11"){Toast.makeText(this,"HAPPY BIRTHDAY HANNAH , LOVE FROM GRANDAD XX",Toast.LENGTH_LONG).show()}
          else{}
        //end of a bit of amusement
        }//end of button refresh onclick listener
        calcTideNow(argNodeList)
        calcHwLw(argNodeList )
    }//end of on Create

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        portChosen = ports[position]//this is not available inside onCreate why??
        binding.etPortMirror.setText(portChosen)

    }//end of on item selected

    override fun onNothingSelected(parent: AdapterView<*>?) {    }



    fun calcTideNow (argNodeList:List<Float>)
    {//start of fun calcTideNow

    val m2Speed = 28.9841042F/60;val s2Speed = 30.00000F/60;val n2Speed = 28.4397295F/60
    val k2Speed = 30.0821373F/60;val m4Speed = 57.9682085F/60;val ms4Speed = 58.9841042F/60
    val nu2Speed = 28.5125831F/60;val mu2Speed = 27.9682084F/60;val l2Speed = 29.5284789F/60
    val k1Speed = 15.0410686F/60;val tn2Speed = 27.8953548F/60; val o1Speed =  13.9430356F/60
    val t2Speed = 29.9589333F/60; val mn4Speed = 57.4238337F/60; val saSpeed = 0.0410686F/60
    val p1Speed = 14.9589314F/60; val q1Speed = 13.3986609F/60

    val speedList = listOf<Float>(m2Speed,s2Speed,n2Speed,k2Speed,m4Speed,ms4Speed,nu2Speed,mu2Speed,
        l2Speed,k1Speed,tn2Speed,o1Speed,t2Speed,mn4Speed,saSpeed,p1Speed,q1Speed)


    var daysAdded:Long=0
    if (binding.etDaysAdvance.text.toString().trim() != "")
    {daysAdded = binding.etDaysAdvance.text.toString().toLong()}
    else    {daysAdded = 0}

    var nooow: ZonedDateTime = ZonedDateTime.now(Clock.systemUTC())
    var noow: ZonedDateTime = nooow.plusDays(daysAdded)
    var noowSecs:Long  = noow.toEpochSecond()
    var noowMins:Long  = (noowSecs)/60
    var yearString:String = noow.format(DateTimeFormatter.ofPattern("yyyy"))
    val yearStart = ZonedDateTime.of(yearString.toInt(),1,1,0,0,0,0, ZoneOffset.UTC)
    val yearStartSecs:Long = yearStart.toEpochSecond()
    val yearStartMins:Long = yearStartSecs/60
    var theTime = noow
    var theTimeSecs= theTime.toEpochSecond()
    var theTimeMins= theTimeSecs/60




    val minsIn = theTimeMins - yearStartMins
    val daysIn = noow.format(DateTimeFormatter.ofPattern("D"))
    var portChosen = binding.etPortMirror.text.toString()
    var fileName:String
    fileName = portChosen + "harm.txt"
    val harmConsList:List<String> =applicationContext.assets.open(fileName).bufferedReader().readLines()
    var showName = harmConsList[0]
    var longTude = harmConsList[1].toFloat()
    var mTl = harmConsList[2].toFloat()
var mHwS = harmConsList[37].toFloat()
var mHwN = harmConsList[38].toFloat()

//loop for harm heights
//var harmHtNow = 0F
var totHarmHtNow = 0F
var h = 1

while (h < 34) //check this number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
{var harmHtNow = ((cos(degToRad *(minsIn.toFloat() * speedList[(h-1)/2] + argNodeList[h-1]  - harmConsList[h+3].toFloat())))
* harmConsList[h+2].toFloat()* argNodeList[h]).toFloat()
totHarmHtNow += harmHtNow
h=h+2
}//end of heightloop
 totHarmHtNow = totHarmHtNow + mTl // mTl is harmConsList[3]
var roundHtNow = kotlin.math.round(totHarmHtNow*100)/100
    binding.etHtNow.setText(decFormat.format(roundHtNow))


//next do slopes
    var totHarmSlopeNow = 0F
    val slackMargin = 0.15
    h = 1

while (h < 34) //check this number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
{var harmSlopeNow = ((-sin(degToRad *(minsIn.toFloat() * speedList[(h-1)/2] + argNodeList[h-1]  - harmConsList[h+3].toFloat())))* harmConsList[h+2].toFloat()* argNodeList[h]).toFloat()
totHarmSlopeNow += harmSlopeNow
h=h+2

}//end of slope loop
 var risFall:String =  if (totHarmSlopeNow  < -1*slackMargin) "Falling" else if(totHarmSlopeNow  > slackMargin)
     "Rising" else if (totHarmHtNow>mTl) "High" else "Low"



    binding.tvRising.setText(risFall)
 // ht = (cos(degToRad* (minsIn.toFloat()* speed (0,1,2...) + eqarg(0,2.4,6...) [+long*0]-phase(4,6,8,10..))
 //    * amplititude(3,5,7,9....) * node(1,3,5,7......)




    binding.etTestBox.setText(" Longitude "+longTude.toString()+"    MTL "+mTl)



}//end of fun calc tide now

    //THIS IS THE START OF MAJOR FUNCTION TO CALCULATE HW AND LW !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    fun calcHwLw(argNodeList:List<Float>){
        var portChosen = binding.etPortMirror.text.toString()
        var fileName:String
        fileName = portChosen + "harm.txt"
        val harmConsList:List<String> =applicationContext.assets.open(fileName).bufferedReader().readLines()

        var mTl = harmConsList[2].toFloat()

        var mHwS = harmConsList[37].toFloat()
        var mHwN = harmConsList[38].toFloat()



    var maxAMHrlyHw = mTl
    var minAMHrlylw = mTl
    var amHrlyHwTime =ZonedDateTime.now(Clock.systemUTC())
    var amHrlyLwTime = ZonedDateTime.now(Clock.systemUTC())
    var i:Int =0
    var maxAMsixHw = mTl//to nearest six mins
    var amSixHwTime = ZonedDateTime.now(Clock.systemUTC())
    var minAMsixlw = mTl
    var amsixLwTime = ZonedDateTime.now(Clock.systemUTC())
    var maxAMHw = mTl//to nearest minute
    var amHwTime = ZonedDateTime.now(Clock.systemUTC())
    var minAMlw = mTl
    var amLwTime = ZonedDateTime.now(Clock.systemUTC())
    var amLWShoTime = ""
    var amHWShoTime:String = ""
    var amHWday:String = ""
    var amLWday = ""
    var amLWht = mTl
    //var amHrlyLwShoTime = ""
    var amHWht= mTl


    var pmLWShoTime =""
    var pmHWShoTime = ""
    var pmLWday=""
    var pmHWday=""
    var pmLWht = mTl
    var pmHWht=mTl
        //var amHrlyShoTime = ""//only neded if test of hourly is still needed
       // var amHrlyHwShoTime = ""//only neded if test of hourly is still needed
        var maxPMHrlyHw = mTl
        var minPMHrlylw = mTl//to nearest hour
        var pmHrlyHwTime =ZonedDateTime.now(Clock.systemUTC())
        var pmHrlyLwTime = ZonedDateTime.now(Clock.systemUTC())
        //var i:Int =0
        var maxPMsixHw = mTl//to nearest six mins
        var pmSixHwTime = ZonedDateTime.now(Clock.systemUTC())
        var minPMsixlw = mTl
        var pmsixLwTime = ZonedDateTime.now(Clock.systemUTC())
        var maxPMHw = mTl//to neares minute
        var pmHwTime = ZonedDateTime.now(Clock.systemUTC())
        var minPMlw = mTl
        var pmLwTime = ZonedDateTime.now(Clock.systemUTC())
        //var pmHrlyLwShoTime = ""//only neded if test of hourly is still needed
        //var pmHrlyShoTime = ""//only neded if test of hourly is still needed

    var daysAdded:Long=0
    if (binding.etDaysAdvance.text.toString().trim() != "")
    {daysAdded = binding.etDaysAdvance.text.toString().toLong()}
    else    {daysAdded = 0}
    var nooow: ZonedDateTime = ZonedDateTime.now(Clock.systemUTC())
    var noow: ZonedDateTime = nooow.plusDays(daysAdded)

    var yearString:String = noow.format(DateTimeFormatter.ofPattern("yyyy"))
    //var yearIndix:Int=yearString.toInt() - 2020//BEFORE YOU START ON ALL THIS CHECK IF YEAR HAS CHANGED...ALSO AT END OF ALL THIS STORE THE YEAR IN SHARED PREFERENCES
    val yearStart = ZonedDateTime.of(yearString.toInt(),1,1,0,0,0,0, ZoneOffset.UTC)
    val yearStartSecs:Long = yearStart.toEpochSecond()
    val yearStartMins:Long = yearStartSecs/60
    val daysIn = noow.format(DateTimeFormatter.ofPattern("D"))

    val dayStart = yearStart.plusDays(daysIn.toLong()-1)


//calculate AM HW&LW
    i=0
    while (i<721) {

var theTime = dayStart.plusMinutes(i.toLong())//THIS MAY BE A CONTRADICTION WITH LATER LOOPS
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins



       var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>maxAMHrlyHw){
        maxAMHrlyHw=htAtTheTime.toFloat()
        amHrlyHwTime = theTime
        //amHrlyShoTime = amHrlyHwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))

    }
     else{}
     if(htAtTheTime<minAMHrlylw) {
         minAMHrlylw = htAtTheTime.toFloat()
         amHrlyLwTime = theTime
         //amHrlyLwShoTime = amHrlyLwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))
     }
    else{}


        i = i + 60
    }//end of while i<721 AM HW/LW loop

    //calculate HW every 6mins for 30 mins either side of amHrlyHwTime
i =-30
    while( i<31) {
     var theTime:ZonedDateTime = amHrlyHwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>maxAMsixHw){
            maxAMsixHw=htAtTheTime.toFloat()
            amSixHwTime = theTime
        }
        else{}

        i = i+6
    }//end of while i<31 forHW loop which goes 30 mins either side of the hour

i = -4
    while (i<4) {
        var theTime:ZonedDateTime = amSixHwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>maxAMHw){
            maxAMHw=htAtTheTime.toFloat()
            amHwTime = theTime
            //amHWShoTime = amHwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))
            amHWday=amHwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            amHWht= round(maxAMHw*100)/100

        }// end of if
        else{}

    i++}//end of while i <4 forHW loop


    //calculate LW every 6mins for 30 mins either side of amHrlyLwTime
    i =-30
    while( i<31) {
        var theTime:ZonedDateTime = amHrlyLwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime<minAMsixlw){
            minAMsixlw=htAtTheTime.toFloat()
            amsixLwTime = theTime
        }
        else{}

        i = i+6
    }//end of while i<31 forLW loop which goes 30 mins either side of the hour

    i = -4
    while (i<4) {
        var theTime:ZonedDateTime = amsixLwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime<minAMlw){
            minAMlw=htAtTheTime.toFloat()
            amLwTime = theTime
           // amLWShoTime = amLwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))
            amLWday=amLwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            amLWht= round(minAMlw*100)/100

        }// end of if
        else{}

        i++}//end of while i <4 forHW loop minute by minute

// calculate PM HW&LW

   i=720
    while (i<1441) {

var theTime = dayStart.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins



       var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)
    if (htAtTheTime>maxPMHrlyHw){
        maxPMHrlyHw=htAtTheTime.toFloat()
        pmHrlyHwTime = theTime
        //pmHrlyShoTime = pmHrlyHwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))

    }
     else{}
     if(htAtTheTime<minPMHrlylw) {
         minPMHrlylw = htAtTheTime.toFloat()
         pmHrlyLwTime = theTime
         //pmHrlyLwShoTime = pmHrlyLwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))
     }
    else{}


        i = i + 60
    }//end of while i<1441  PM loop

    //calculate HW every 6mins for 30 mins either side of pmHrlyHwTime
i =-30
    while( i<31) {
     var theTime:ZonedDateTime = pmHrlyHwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>maxPMsixHw){
            maxPMsixHw=htAtTheTime.toFloat()
            pmSixHwTime = theTime
        }
        else{}

        i = i+6
    }//end of while i<31 for PM HW loop which goes 30 mins either side of the hour

i = -4
    while (i<4) {
        var theTime:ZonedDateTime = pmSixHwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>maxPMHw){
            maxPMHw=htAtTheTime.toFloat()
            pmHwTime = theTime

            pmHWday=pmHwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            pmHWht= round(maxPMHw*100)/100

        }// end of if
        else{}

    i++}//end of while i <4 for PM HW loop


    //calculate LW every 6mins for 30 mins either side of pmHrlyLwTime
    i =-30
    while( i<31) {
        var theTime:ZonedDateTime = pmHrlyLwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime<minPMsixlw){
            minPMsixlw=htAtTheTime.toFloat()
            pmsixLwTime = theTime
        }
        else{}

        i = i+6
    }//end of while i<31 for PM LW loop which goest 30 mins either side of the hour

    i = -4
    while (i<4) {
        var theTime:ZonedDateTime = pmsixLwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime<minPMlw){
            minPMlw=htAtTheTime.toFloat()
            pmLwTime = theTime
           // pmLWShoTime = pmLwTime.format(java.time.format.DateTimeFormatter.ofPattern("HH mm"))
            pmLWday=pmLwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            pmLWht= round(minPMlw*100)/100

        }// end of if
        else{}

        i++
    }//end of while i <4 for PM LW loop


//start of close times fudge 1
//another fudge required to deal with HW at say 1250 being also recorded as an AMHW with a time of 1233
        if (pmHwTime.toEpochSecond()-amHwTime.toEpochSecond() < 120 * 60 && pmHwTime.format(DateTimeFormatter.ofPattern("HH mm"))=="12 33" )
        {
            amHwTime = pmHwTime.minusMinutes((12.5*60).toLong())//recalculate based around pmHW less 12.5 hrs
            amHWht = mTl
            i =-60
            while( i<61) {
                var theTime:ZonedDateTime = amHwTime.plusMinutes(i.toLong())
                var theTimeSecs= theTime.toEpochSecond()
                var theTimeMins= theTimeSecs/60
                var minsIn = theTimeMins - yearStartMins

                var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

                if (htAtTheTime>amHWht){
                    amHWht=htAtTheTime.toFloat()
                    amHwTime = theTime
                }
                else{}

                i = i+1

            }//end of while loop minute by minute
            amHWday=amHwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            amHWht= round(amHWht*100)/100
        }//end of outer if
        else{}
        //end of close times fudge 1

//start of close times fudge 2
//another fudge required to deal with HW at say 1120 being also recorded as an PMHW with a time of 1126
if (pmHwTime.toEpochSecond()-amHwTime.toEpochSecond() < 120 * 60 && pmHwTime.format(DateTimeFormatter.ofPattern("HH mm"))=="11 26" )// && pmHWTime.format(DateTimeFormatter.ofPattern("HH mm"))=="11 26"
    {
    pmHwTime = amHwTime.plusMinutes((12.5*60).toLong())//recalculate based around amHW plus 12.5 hrs
    pmHWht = mTl
    i =-60
    while( i<61) {
        var theTime:ZonedDateTime = pmHwTime.plusMinutes(i.toLong())
        var theTimeSecs= theTime.toEpochSecond()
        var theTimeMins= theTimeSecs/60
        var minsIn = theTimeMins - yearStartMins

        var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

        if (htAtTheTime>pmHWht){
            pmHWht=htAtTheTime.toFloat()
            pmHwTime = theTime
        }
        else{}

        i = i+1

    }//end of while loop minute by minute
    pmHWday=pmHwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
    pmHWht= round(pmHWht*100)/100
    }//end of outer if
    else{}
        //end of fudge where pm HW time = 1126
        //end of close times fudge 2

//start of close times fudge 3
//another fudge required to deal with LW at say 1250 being also recorded as a AMLW with a time of 1233
        if (pmLwTime.toEpochSecond()-amLwTime.toEpochSecond() < 120 * 60 && amLwTime.format(DateTimeFormatter.ofPattern("HH mm"))=="12 33")
        {
            amLwTime = pmLwTime.minusMinutes((12.5*60).toLong())//recalculate based around pmLW less 12.5 hrs
            amLWht = mTl
            i =-60
            while( i<61) {
                var theTime:ZonedDateTime = amLwTime.plusMinutes(i.toLong())
                var theTimeSecs= theTime.toEpochSecond()
                var theTimeMins= theTimeSecs/60
                var minsIn = theTimeMins - yearStartMins

                var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

                if (htAtTheTime<amLWht){
                    amLWht=htAtTheTime.toFloat()
                    amLwTime = theTime
                } else{}
                i = i+1
            }//end of while loop minute by minute

            amLWday=amLwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            amLWht= round(amLWht*100)/100
        }//end of outer if
        else{}//end of close times fudge 3

//start of close times fudge 4
//another fudge required to deal with LW at say 1120 being also recorded as an PMLW with a time of 1126
        if (pmLwTime.toEpochSecond()-amLwTime.toEpochSecond() < 120 * 60 && pmLwTime.format(DateTimeFormatter.ofPattern("HH mm"))=="11 26" )
        {
            pmLwTime = amLwTime.plusMinutes((12.5*60).toLong())//recalculate based around amHW plus 12.5 hrs
            pmLWht = mTl
            i =-60
            while( i<61) {
                var theTime:ZonedDateTime = pmLwTime.plusMinutes(i.toLong())
                var theTimeSecs= theTime.toEpochSecond()
                var theTimeMins= theTimeSecs/60
                var minsIn = theTimeMins - yearStartMins

                var htAtTheTime =  calcHtAtTheTime(minsIn.toInt(),argNodeList)

                if (htAtTheTime<pmLWht){
                    pmLWht=htAtTheTime.toFloat()
                    pmLwTime = theTime
                }
                else{}

                i = i+1

            }//end of while loop minute by minute
            pmLWday=pmLwTime.format(java.time.format.DateTimeFormatter.ofPattern("E"))
            pmLWht= round(pmLWht*100)/100
        }//end of outer if
        else{}
        //end of fudge where pm LW time = 1126
        //end of close times fudge 4


        /*
       //taken from st helier almanac MHWS 11.0 MHWN 8.1 mTl is 6.03
       //start of fudge for  am HWTime
        // fudge is to subtract  c.15 mins for a mean neap tide (half range 2.07), reducing in proportion to how far the range has
        //progressed toward a mean spring half range (4.97) where fudge = zero.
        */
        var HwBaseFudge = 9F
        var LwBaseFudge = 13F
        var amHWfudge:Float = 0F
        var fudgeFactor = (amHWht-mHwN)/(mHwS-mHwN)
        var pmHWfudge:Float = 0F
        var amLWfudge:Float = 0F
        var pmLWfudge:Float = 0F

        amHWfudge = HwBaseFudge*fudgeFactor - HwBaseFudge
        amHwTime = amHwTime.plusMinutes(amHWfudge.toLong())
        pmHWfudge = HwBaseFudge*fudgeFactor - HwBaseFudge
        pmHwTime = pmHwTime.plusMinutes(pmHWfudge.toLong())

        amLWfudge = LwBaseFudge*fudgeFactor - LwBaseFudge
        amLwTime = amLwTime.plusMinutes(amLWfudge.toLong())
        pmLWfudge = LwBaseFudge*fudgeFactor - LwBaseFudge
        pmLwTime = pmLwTime.plusMinutes(pmLWfudge.toLong())
// here adjust to local time if required
        //var deltaH = LocalDate.now().getTimezoneOffset// getTimezoneOffset unresolved reference
//amLwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH mm"))
 /*
 In that case it is very easy: amLwTime.withZoneSameInstant(ZoneId.of("Europe/London")).format(DateTimeFormatter.ofPattern("HH mm"). Same for Paris.
 If you want the JVM’s default time zone. use ZoneId.systemDefault(). Summer time (DST) comes automatically; Java knows when it starts and ends in each time zone. – Ole V.V. 28 mins ago
What I was hoping to achieve was that when in France, it would show me the answers in French time, and when in Britain, British Time. So does this mean I should use:- – JJCNSNR 16 mins ago
If you trust the user to set the time zone of the device to the place where s/he is, then amLwTime.withZoneSameInstant(ZoneId.systemDefault()), etc. – Ole V.V. 11 mins ago
1
I guessthis does it and this stackoverflow.com/questions/49853999/… does answer the question. Should I delete my question. ?? Anyway thanks very much for educating me – JJCNSNR 6 mins ago




        var locH = LocalDateTime.now().hour
        var calH =  ZonedDateTime.now(Clock.systemUTC()).hour
        var midnH = if(calH>locH)24 else 0//only works west of meridian eg britain and france
        var deltaH = (locH-calH+midnH).toLong()
        amLWShoTime = amLwTime.plusHours(deltaH).format(DateTimeFormatter.ofPattern("HH mm"))
        amHWShoTime = amHwTime.plusHours(deltaH).format(DateTimeFormatter.ofPattern("HH mm"))
        pmHWShoTime = pmHwTime.plusHours(deltaH).format(DateTimeFormatter.ofPattern("HH mm"))
        pmLWShoTime = pmLwTime.plusHours(deltaH).format(DateTimeFormatter.ofPattern("HH mm"))


        amHWday=amHwTime.plusHours(deltaH).format(java.time.format.DateTimeFormatter.ofPattern("E"))
        amLWday=amLwTime.plusHours(deltaH).format(java.time.format.DateTimeFormatter.ofPattern("E"))
        pmHWday=pmHwTime.plusHours(deltaH).format(java.time.format.DateTimeFormatter.ofPattern("E"))
        pmLWday=pmLwTime.plusHours(deltaH).format(java.time.format.DateTimeFormatter.ofPattern("E"))

 */
        amLWShoTime = amLwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH mm"))
        amHWShoTime = amHwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH mm"))
        pmLWShoTime = pmLwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH mm"))
        pmHWShoTime = pmHwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH mm"))

        amHWday=amHwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("E"))
        amLWday=amLwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("E"))
        pmHWday=pmHwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("E"))
        pmLWday=pmLwTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("E"))

    binding.tvFullDate.setText(noow.format(DateTimeFormatter.ofPattern("EE dd LLL yyyy")))



if (amHwTime>amLwTime){
    binding.tvTideLabel1.setText("LW");binding.tvTideTim1.setText(amLWShoTime)
    binding.tvTideLabel2.setText("HW");binding.tvTideTim2.setText(amHWShoTime)
    binding.tvTideHt1.setText(decFormat.format(amLWht));binding.tvTide1Day.setText(amLWday)
    binding.tvTideHt2.setText(decFormat.format(amHWht));binding.tvTide2Day.setText(amHWday)

    binding.tvTideLabel3.setText("LW");binding.tvTideTim3.setText(pmLWShoTime)
    binding.tvTideLabel4.setText("HW");binding.tvTideTim4.setText(pmHWShoTime)
    binding.tvTideHt3.setText(decFormat.format(pmLWht));binding.tvTide3Day.setText(pmLWday)
    binding.tvTideHt4.setText(decFormat.format(pmHWht));binding.tvTide4Day.setText(pmHWday)
                    }// end of if HW later than LW
    else{
    binding.tvTideLabel2.setText("LW");binding.tvTideTim2.setText(amLWShoTime)
    binding.tvTideLabel1.setText("HW");binding.tvTideTim1.setText(amHWShoTime)
    binding.tvTideHt2.setText(decFormat.format(amLWht));binding.tvTide2Day.setText(amLWday)
    binding.tvTideHt1.setText(decFormat.format(amHWht));binding.tvTide1Day.setText(amHWday)

    binding.tvTideLabel4.setText("LW");binding.tvTideTim4.setText(pmLWShoTime)
    binding.tvTideLabel3.setText("HW");binding.tvTideTim3.setText(pmHWShoTime)
    binding.tvTideHt4.setText(decFormat.format(pmLWht));binding.tvTide4Day.setText(pmLWday)
    binding.tvTideHt3.setText(decFormat.format(pmHWht));binding.tvTide3Day.setText(pmHWday)
    }

}//end of calcHwLw

    //THIS IS THE MAJOR subFunction of calcHwLw CALCULATE HT AT ANY GIVEN TIME loops thru harmonics
    public fun calcHtAtTheTime(minsIn: Int,argNodeList:List<Float>):Double
{
    val m2Speed = 28.9841042F/60;val s2Speed = 30.00000F/60;val n2Speed = 28.4397295F/60
    val k2Speed = 30.0821373F/60;val m4Speed = 57.9682085F/60;val ms4Speed = 58.9841042F/60
    val nu2Speed = 28.5125831F/60;val mu2Speed = 27.9682084F/60;val l2Speed = 29.5284789F/60
    val k1Speed = 15.0410686F/60;val tn2Speed = 27.8953548F/60; val o1Speed =  13.9430356F/60
    val t2Speed = 29.9589333F/60; val mn4Speed = 57.4238337F/60; val saSpeed = 0.0410686F/60
    val p1Speed = 14.9589314F/60; val q1Speed = 13.3986609F/60
    val speedList = listOf<Float>(m2Speed,s2Speed,n2Speed,k2Speed,m4Speed,ms4Speed,nu2Speed,mu2Speed,
            l2Speed,k1Speed,tn2Speed,o1Speed,t2Speed,mn4Speed,saSpeed,p1Speed,q1Speed)
//are the speeds worked out twice? yes once in calc tides now

    var portChosen = binding.etPortMirror.text.toString()
    var fileName:String
    fileName = portChosen + "harm.txt"
    val harmConsList:List<String> =applicationContext.assets.open(fileName).bufferedReader().readLines()
    var htAtTheTime = 0F
    var mTl = harmConsList[2].toFloat()
    var h: Int = 1

    while (h < 34)
    {var harmHtAtTheTime = ((cos(degToRad *(minsIn.toFloat() * speedList[(h-1)/2] + argNodeList[h-1]  - harmConsList[h+3].toFloat())))
            * harmConsList[h+2].toFloat()* argNodeList[h]).toFloat()
        htAtTheTime += harmHtAtTheTime
        h=h+2
    }//end of heightloop
    htAtTheTime = htAtTheTime  + mTl // mTl is harmConsList[3]

    //binding.etTestBox.setText("chosen harm value    "+t2HtNow.toString())

    return htAtTheTime.toDouble()

}//end of fun calcHtAtTheTime
    private fun View.closeKeyboard() {
        val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }//end of functionclosekeyboard
}//end of main activity class





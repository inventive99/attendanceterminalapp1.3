package com.inventive.attendanceUser.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ConstantVariables {


    public static String calculateNextDay(String date) {
        try {

            String nextDate="";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf1.parse(date));
            c.add(Calendar.DATE, 1);  // number of days to add
            nextDate = sdf1.format(c.getTime());  // dt is now the new date

            return nextDate;
        }catch (Exception e){
            e.printStackTrace();
            return  date;
        }
    }

    public static String calculatePreviousDay(String date) {
        try {

            String previousDate="";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf1.parse(date));
            c.add(Calendar.DATE, -1);  // number of days to add
            previousDate = sdf1.format(c.getTime());  // dt is now the new date

            return previousDate;
        }catch (Exception e){
            e.printStackTrace();
            return  date;
        }
    }

    public static class OKHTTPVariables {
        public static int MaxTimeOutInMins = 10;
        public static TimeUnit ConnectionTimeOutUnit = TimeUnit.MINUTES;
    }


    /* multipart request Creator .... start*/
    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

    }

//    /* multipart request Creator .... start*/
//    @NonNull
//    public static MultipartBody.Part prepareArrayPart(String partName, float[][] file) {
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
//
//    }


    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }

    public static class ExpenseDetailsTabs {
        public static String TabDetails = "DETAILS";
        public static String TabNotes = "NOTES";
        public static String TabAmounts = "AMOUNT";
    }


    public static class FileExtensions {
        public static final String DOC = "application/msword";
        public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        public static final String IMAGE_JPEG = "image/jpeg";
        public static final String IMAGE_JPG = "image/jpg";
        public static final String IMAGE_PNG = "image/png";
        public static final String AUDIO = "audio/*";
        public static final String VIDEO_MP4 = "video/mp4";
        public static final String VIDEO_MOV = "video/mov";
        public static final String TEXT = "text/";
        public static final String PDF = "application/pdf";
        public static final String XLS = "application/vnd.ms-excel";
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean istimeexist1(String intime, String time,String outtime) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            Date start = sdf.parse(intime);
            Date now = sdf.parse(time);
            Date out = sdf.parse(outtime);

            if(now.after(start) && now.before(out))
            {
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static boolean istime24exist1(String intime, String time,String outtime) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Log.d("kkkkkkkkkkkk21", intime);
            Log.d("kkkkkkkkkkkk22", time);
            Log.d("kkkkkkkkkkkk23", outtime);

            Date start = sdf.parse(intime);
            Date now = sdf.parse(time);
            Date out = sdf.parse(outtime);

            if(now.after(start) && now.before(out))
            {
                Log.d("kkkkkkkkkkkk223", "111111111111");
                return true;
            }else {
                Log.d("kkkkkkkkkkkk224", "22222222222222222");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static String getlasttime(String lastout, String outtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

                try {
                    Date lastoutd = sdf.parse(lastout);
                    Date outtimed = sdf.parse(outtime);

                    if (outtimed.after(lastoutd)){
                        lastout=outtime;
                        return lastout;
                    }else {
                        return lastout;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    return lastout;
                }

    }

    public static String getlastnighttime(String lastout, String outtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try {

            Date lastoutd = sdf.parse(lastout);
            Date outtimed = sdf.parse(outtime);

            if (outtimed.after(lastoutd)){
                lastout=outtime;
                return lastout;
            }else {
                return lastout;
            }

        }catch (Exception e){
            e.printStackTrace();
            return lastout;
        }

    }

    public static boolean istimeexist(Context context,String now,String indisplaybefore, String indisplayafter, String outdisplaybefore,
                                      String outdisplayafter, String lindisplaybefore, String lindisplayafter,
                                      String loutdisplaybefore, String loutdisplayafter) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        try{

            Date nowd = sdf.parse(now);

            Date indisplaybefored = sdf.parse(indisplaybefore);
            Date indisplayafterd = sdf.parse(indisplayafter);
            Date outdisplaybefored = sdf.parse(outdisplaybefore);
            Date outdisplayafterd = sdf.parse(outdisplayafter);

            Date lindisplaybefored = null,lindisplayafterd = null,loutdisplaybefored = null,loutdisplayafterd = null;
            if (!loutdisplaybefore.equals("0")){
                 lindisplaybefored = sdf.parse(lindisplaybefore);
                 lindisplayafterd = sdf.parse(lindisplayafter);
                 loutdisplaybefored = sdf.parse(loutdisplaybefore);
                 loutdisplayafterd = sdf.parse(loutdisplayafter);

            }

            if(nowd.after(indisplaybefored) && nowd.before(indisplayafterd))
            {
                ConstantValues.putWindowOpen("1", indisplayafter,context);

                return true;
            }else if(nowd.after(outdisplaybefored) && nowd.before(outdisplayafterd))
            {
                ConstantValues.putWindowOpen("1", outdisplayafter,context);
                return true;
            }else {

                if (!loutdisplaybefore.equals("0")){
                    if(nowd.after(lindisplaybefored) && nowd.before(lindisplayafterd))
                    {
                        ConstantValues.putWindowOpen("1", lindisplayafter,context);
                        return true;
                    }else if(nowd.after(loutdisplaybefored) && nowd.before(loutdisplayafterd))
                    {
                        ConstantValues.putWindowOpen("1", loutdisplayafter,context);
                        return true;
                    }else{
                        return false;
                    }
                }else {
                    return false;
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

        public static String[] Check_time(String intime, String time,String gracep) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);
            Date grace = sdf.parse(gracep);

            String msg="",grace_time="";
            String[] myArray = new String[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="E "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Early";
                }else {
                    grace_time="E 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Early";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else if(end.before(grace))
            {
                long diff = Math.abs(end.getTime() - start.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="T "+String.valueOf(hrs)+"-"+String.valueOf(min);
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    grace_time="T 0-"+String.valueOf(diffMinutes);
                    msg="You are "+diffMinutes+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else {

                long diff = Math.abs(end.getTime() - grace.getTime());
                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){
                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;
                    grace_time="L "+String.valueOf(hrs)+"-"+String.valueOf(min);
                }else {
                    grace_time="L 0-"+String.valueOf(diffMinutes);
                }


                long diff1 = Math.abs(end.getTime() - start.getTime());
                long diffSeconds1 = (diff1 / 1000) % 60;
                long diffMinutes1 = (diff1 / (60 * 1000));

                if (diffMinutes1 >= 60){
                    long hrs=diffMinutes1/60;
                    long min=diffMinutes1%60;
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    msg="You are "+diffMinutes1+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }

            return myArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
    }


    public static String[] Check_datetime(String intime, String time,String gracep) {
        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Log.d("aaaaaaaapppptttttttt",intime);
            Log.d("aaaaaaaapppptttttttt",time);
            Log.d("aaaaaaaapppptttttttt",gracep);

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);
            Date grace = sdf.parse(gracep);

            String msg="",grace_time="";
            String[] myArray = new String[2];
            if(end.before(start))
            {
                Log.d("aaaaaaaapppptttttttt","1111111111111");

                long diff = Math.abs(start.getTime() - end.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="E "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Early";
                }else {
                    grace_time="E 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Early";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else if(end.before(grace))
            {
                Log.d("aaaaaaaapppptttttttt","222222222222222");

                long diff = Math.abs(end.getTime() - start.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="T "+String.valueOf(hrs)+"-"+String.valueOf(min);
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    grace_time="T 0-"+String.valueOf(diffMinutes);
                    msg="You are "+diffMinutes+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else {

                Log.d("aaaaaaaapppptttttttt","33333333333333");

                long diff = Math.abs(end.getTime() - grace.getTime());
                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){
                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;
                    grace_time="L "+String.valueOf(hrs)+"-"+String.valueOf(min);
                }else {
                    grace_time="L 0-"+String.valueOf(diffMinutes);
                }


                long diff1 = Math.abs(end.getTime() - start.getTime());
                long diffSeconds1 = (diff1 / 1000) % 60;
                long diffMinutes1 = (diff1 / (60 * 1000));

                if (diffMinutes1 >= 60){
                    long hrs=diffMinutes1/60;
                    long min=diffMinutes1%60;
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    msg="You are "+diffMinutes1+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }

            Log.d("aaaaaaaapppptttttttt66",myArray[0]);
            Log.d("aaaaaaaapppptttttttt661",myArray[1]);
            return myArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
    }


    public static String[] Check_out_time(String intime, String time) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);

            String msg="",grace_time="";
            String[] myArray = new String[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="E "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Early";
                }else {
                    grace_time="E 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Early";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else {

                long diff = Math.abs(end.getTime() - start.getTime());
                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){
                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;
                    grace_time="L "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    grace_time="L 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Late";
                }


//                long diff1 = Math.abs(end.getTime() - start.getTime());
//                long diffSeconds1 = (diff1 / 1000) % 60;
//                long diffMinutes1 = (diff1 / (60 * 1000));
//
//                if (diffMinutes1 >= 60){
//                    long hrs=diffMinutes1/60;
//                    long min=diffMinutes1%60;
//
//                }else {
//
//                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }

            return myArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
    }

    public static String[] Check_Nighttime(String intime, String time,String gracep) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);
            Date grace = sdf.parse(gracep);

            String msg="",grace_time="";
            String[] myArray = new String[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="E "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Early";
                }else {
                    grace_time="E 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Early";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else if(end.before(grace))
            {
                long diff = Math.abs(end.getTime() - start.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="T "+String.valueOf(hrs)+"-"+String.valueOf(min);
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    grace_time="T 0-"+String.valueOf(diffMinutes);
                    msg="You are "+diffMinutes+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else {

                long diff = Math.abs(end.getTime() - grace.getTime());
                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){
                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;
                    grace_time="L "+String.valueOf(hrs)+"-"+String.valueOf(min);
                }else {
                    grace_time="L 0-"+String.valueOf(diffMinutes);
                }


                long diff1 = Math.abs(end.getTime() - start.getTime());
                long diffSeconds1 = (diff1 / 1000) % 60;
                long diffMinutes1 = (diff1 / (60 * 1000));

                if (diffMinutes1 >= 60){
                    long hrs=diffMinutes1/60;
                    long min=diffMinutes1%60;
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    msg="You are "+diffMinutes1+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }

            return myArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
    }


    public static String[] Check_Night_out_time(String intime, String time) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);

            String msg="",grace_time="";
            String[] myArray = new String[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    grace_time="E "+String.valueOf(hrs)+"-"+String.valueOf(min);

                    msg="You are "+hrs+" hrs. "+min+" min. Early";
                }else {
                    grace_time="E 0-"+String.valueOf(diffMinutes);

                    msg="You are "+diffMinutes+" min Early";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }else {

                long diff = Math.abs(end.getTime() - start.getTime());
                long diffSeconds = (diff / 1000) % 60;
                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){
                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;
                    grace_time="L "+String.valueOf(hrs)+"-"+String.valueOf(min);
                }else {
                    grace_time="L 0-"+String.valueOf(diffMinutes);
                }


                long diff1 = Math.abs(end.getTime() - start.getTime());
                long diffSeconds1 = (diff1 / 1000) % 60;
                long diffMinutes1 = (diff1 / (60 * 1000));

                if (diffMinutes1 >= 60){
                    long hrs=diffMinutes1/60;
                    long min=diffMinutes1%60;
                    msg="You are "+hrs+" hrs. "+min+" min. Late";
                }else {
                    msg="You are "+diffMinutes1+" min Late";
                }

                myArray[0]=msg;
                myArray[1]=grace_time;

            }

            return myArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return new String[0];
    }



    public static long[] CalculateHrsMin(String intime, String time) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);

            long[] myArray = new long[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    myArray[0]=hrs;
                    myArray[1]=min;
                }else {
                    myArray[0]=0;
                    myArray[1]=diffMinutes;
                }

                return myArray;

            }else {

                long diff = Math.abs(end.getTime() - start.getTime());

                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    myArray[0]=hrs;
                    myArray[1]=min;
                }else {
                    myArray[0]=0;
                    myArray[1]=diffMinutes;
                }

                return myArray;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new long[0];
    }

    public static long[] CalculateNightHrsMin(String intime, String time) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date start = sdf.parse(intime);
            Date end = sdf.parse(time);

            long[] myArray = new long[2];
            if(end.before(start))
            {
                long diff = Math.abs(start.getTime() - end.getTime());

                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    myArray[0]=hrs;
                    myArray[1]=min;
                }else {
                    myArray[0]=0;
                    myArray[1]=diffMinutes;
                }

                return myArray;

            }else {

                long diff = Math.abs(end.getTime() - start.getTime());

                long diffMinutes = (diff / (60 * 1000));

                if (diffMinutes >= 60){

                    long hrs=diffMinutes/60;
                    long min=diffMinutes%60;

                    myArray[0]=hrs;
                    myArray[1]=min;
                }else {
                    myArray[0]=0;
                    myArray[1]=diffMinutes;
                }

                return myArray;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new long[0];
    }

    public static String calculateNewTime(String lunchout, int i) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
            Date d = df.parse(lunchout);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, i);
            String newTime = df.format(cal.getTime());


            return newTime;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String calculateNewNightTime(String lunchout, int i) {
        try {
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm aa");

            Date d = df.parse(lunchout);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, i);
            String newTime = df.format(cal.getTime());


            return newTime;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String calculateSingleTime(String lunchout, int i) {
        try {
            Log.d("aaaaaaaaaaaaaa1",lunchout);

            SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
            Date d = df.parse(lunchout);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.SECOND, i);
            String newTime = df.format(cal.getTime());


            return newTime;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String calculateNewTime24(String intime,String time, int i, String date) {
        try {

            Log.d("aaaaaaaayiiiiiaa1",intime);
            Log.d("aaaaaaaayiiiiiaa1",time);
            Log.d("aaaaaaaayiiiiiaa1", String.valueOf(i));
            Log.d("aaaaaaaayiiiiiaa1",date);

//            SimpleDateFormat sdfdatetime = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
//            SimpleDateFormat sdfdatetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
//
//            Date d = sdfdatetime.parse(date+" "+intime);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(d);
//            cal.add(Calendar.MINUTE, i);
//            String newTime100 = sdfdatetime.format(cal.getTime());
//
//            Date newTime100d = sdfdatetime.parse(newTime100);
//            String newTime100dd = sdfdatetime1.format(newTime100d);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
            Date d = df.parse(intime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, i);
            String newTime = df.format(cal.getTime());
            Date newTimed = df.parse(newTime);
            //String newTime11 = displayFormat.format(newTime);
            String newTime1;

            if (time.contains("p") || time.contains("P")){
                if (newTime.contains("p") || newTime.contains("P")){
                    newTime1=date+" "+displayFormat.format(newTimed);
                }else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf1.parse(date));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    String nextDate = sdf1.format(c.getTime());

                    newTime1=nextDate+" "+displayFormat.format(newTimed);
                }
            }else {
                if (newTime.contains("a") || newTime.contains("A")){
                    newTime1=date+" "+displayFormat.format(newTimed);
                }else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf1.parse(date));
                    c.add(Calendar.DATE, -1);  // number of days to add
                    String previousdate = sdf1.format(c.getTime());

                    newTime1=previousdate+" "+displayFormat.format(newTimed);
                }
            }

            Log.d("aaafffff",newTime1);

            return newTime1;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String calculateTime24(String intime,String time, int i, String date) {
        try {

            SimpleDateFormat sdfdatetime = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
            SimpleDateFormat sdfdatetime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
            Date d = sdfdatetime.parse(date+" "+intime);
            Date d1 = df.parse(intime);

            Date timed = df.parse(time);

            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, i);
            String newTime = df.format(cal.getTime());
            String newTime100 = sdfdatetime.format(cal.getTime());

            Date newTime100d = sdfdatetime.parse(newTime100);
            String newTime100dd = sdfdatetime1.format(newTime100d);
//            Date newTimed = df.parse(newTime);
//
//            int year = cal.get(cal.YEAR); // current year
//            int month = cal.get(cal.MONTH); // current month
//            int day = cal.get(cal.DAY_OF_MONTH); // current day
//

//            Log.d("aaafffff00",year + "-"+ (month + 1) + "-" + day);
            Log.d("aaafffff00",newTime100);
            Log.d("aaafffff00",newTime100dd);

            //String newTime11 = displayFormat.format(newTime);
//            String newTime1;
//
//            if (time.contains("p") || time.contains("P")){
//                if (newTime.contains("p") || newTime.contains("P")){
//                    newTime1=date+" "+displayFormat.format(newTimed);
//                    Log.d("aaafffff1",newTime);
//
//                }else {
////                    if (i<0){
////
////                        newTime1=date+" "+displayFormat.format(newTimed);
////                        Log.d("aaafffff2",newTime);
////                    }else {
//
//                    if (timed.after(newTimed)){
//
//                        newTime1=date+" "+displayFormat.format(newTimed);
//                        Log.d("aaafffff222",newTime);
//
//                    }else {
//                        Calendar c = Calendar.getInstance();
//                        c.setTime(sdf1.parse(date));
//                        c.add(Calendar.DATE, 1);  // number of days to add
//                        String nextDate = sdf1.format(c.getTime());
//
//                        newTime1=nextDate+" "+displayFormat.format(newTimed);
//                        Log.d("aaafffff22",newTime);
//                    }
//
////                    }
//
//                }
//            }else {
//                if (newTime.contains("a") || newTime.contains("A")){
//                    newTime1=date+" "+displayFormat.format(newTimed);
//                    Log.d("aaafffff3",newTime);
//
//                }else {
////                    if (i<0){
//
//
//                    if (timed.after(newTimed)){
//
//                        Calendar c = Calendar.getInstance();
//                        c.setTime(sdf1.parse(date));
//                        c.add(Calendar.DATE, -1);  // number of days to add
//                        String previousdate = sdf1.format(c.getTime());
//
//                        newTime1=previousdate+" "+displayFormat.format(newTimed);
//                        Log.d("aaafffff4",newTime);
//
//                    }else {
//                        newTime1=date+" "+displayFormat.format(newTimed);
//                        Log.d("aaafffff44",newTime);
//                    }
//
////                    }else {
////
////                        newTime1=date+" "+displayFormat.format(newTimed);
////                        Log.d("aaafffff44",newTime);
////                    }
//                }
//            }

            Log.d("aaafffff",newTime100dd);

            return newTime100dd;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        int len = 0;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
        return byteBuffer.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void OpenExitDialogue(Context context,String s) {

    }

    public static void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        Log.d("freeMemory","aaaaaa");
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String doSomethingMemoryIntensive(Context applicationContext) {

        String totalmemory="";
        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory(applicationContext);

        if (!memoryInfo.lowMemory) {
            // Do memory intensive work ...
            Log.d("MemoryInfo", String.valueOf(memoryInfo.totalMem));
            Log.d("MemoryInfo Available", String.valueOf(memoryInfo.availMem));
            totalmemory= String.valueOf(memoryInfo.availMem);
        }else {
            Log.d("Low MemoryInfo", String.valueOf(memoryInfo));
            totalmemory= String.valueOf(memoryInfo.availMem);
        }

        return totalmemory;
    }

    // Get a MemoryInfo object for the device's current memory status.
    public static ActivityManager.MemoryInfo getAvailableMemory(Context applicationContext) {
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(applicationContext.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

}

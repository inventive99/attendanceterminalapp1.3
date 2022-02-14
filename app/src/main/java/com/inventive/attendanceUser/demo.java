package com.inventive.attendanceUser;

public class demo {

//    public void addImage(){
//
//        previewWidth = 640;
//        previewHeight = 480;
//
//        sensorOrientation = 90 - getScreenOrientation();
//
//        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
//
//
//        int targetW1, targetH1;
//        if (sensorOrientation == 90 || sensorOrientation == 270) {
//            targetH1 = previewWidth;
//            targetW1 = previewHeight;
//        }
//        else {
//            targetW1 = previewWidth;
//            targetH1 = previewHeight;
//        }
//        int cropW = (int) (targetW1 / 2.0);
//        int cropH = (int) (targetH1 / 2.0);
//
//        croppedBitmap = Bitmap.createBitmap(cropW, cropH, Bitmap.Config.ARGB_8888);
//
//
//        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
//        final Canvas canvas = new Canvas(cropCopyBitmap);
//        final Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(2.0f);
//
//        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
//        switch (MODE) {
//            case TF_OD_API:
//                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
//                break;
//        }
//
//        final List<SimilarityClassifier.Recognition> mappedRecognitions =
//                new LinkedList<SimilarityClassifier.Recognition>();
//
//
//        //final List<Classifier.Recognition> results = new ArrayList<>();
//
//        // Note this can be done only once
//        int sourceW = rgbFrameBitmap.getWidth();
//        int sourceH = rgbFrameBitmap.getHeight();
//        int targetW = portraitBmp.getWidth();
//        int targetH = portraitBmp.getHeight();
//        Matrix transform = createTransform(
//                sourceW,
//                sourceH,
//                targetW,
//                targetH,
//                sensorOrientation);
//        final Canvas cv = new Canvas(portraitBmp);
//
//        // draws the original image in portrait mode.
//        cv.drawBitmap(rgbFrameBitmap, transform, null);
//
//        final Canvas cvFace = new Canvas(faceBmp);
//
//        boolean saved = false;
//
//    }
}

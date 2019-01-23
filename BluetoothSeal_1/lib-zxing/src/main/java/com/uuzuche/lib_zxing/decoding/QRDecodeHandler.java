/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uuzuche.lib_zxing.decoding;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.uuzuche.lib_zxing.R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.camera.PlanarYUVLuminanceSource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

final class QRDecodeHandler extends Handler {

    private static final String TAG = QRDecodeHandler.class.getSimpleName();

    private final CaptureFragment fragment;
    private final QRCodeMultiReader qrCodeMultiReader;
    private final Hashtable<DecodeHintType, Object> hints;

    QRDecodeHandler(CaptureFragment fragment, Hashtable<DecodeHintType, Object> hints) {
        qrCodeMultiReader = new QRCodeMultiReader();
        this.hints = hints;
        this.fragment = fragment;
    }

    private static void bundleThumbnail(com.google.zxing.PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
//        long start = System.currentTimeMillis();
        Result[] rawResults = null;

        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResults = qrCodeMultiReader.decodeMultiple(bitmap, hints);
        } catch (ReaderException re) {
            // continue
        } finally {
            qrCodeMultiReader.reset();
        }

        if (rawResults != null) {
            List<Result> results = new ArrayList<>(Arrays.asList(rawResults));
            if (results.size() > 0) {
                for (int h = 0; h < results.size(); h++) {
                    Log.e("Result", "index  " + h + "     results    =" + results.get(h).getText());
                }
            }
//            long end = System.currentTimeMillis();
//            Log.e(TAG, "Found barcode (" + (end - start) + " ms):\n" + Arrays.toString(rawResults));
            Message message = Message.obtain(fragment.getHandler(), R.id.decode_succeeded, rawResults);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            //Log.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget();
        } else {
            Message message = Message.obtain(fragment.getHandler(), R.id.decode_failed);
            message.sendToTarget();
        }
    }

}

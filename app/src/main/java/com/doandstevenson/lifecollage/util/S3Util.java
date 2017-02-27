package com.doandstevenson.lifecollage.util;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevenson.lifecollage.Constants;

/**
 * Created by CGrahamS on 1/31/17.
 */

public class S3Util {

    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    private static CognitoCachingCredentialsProvider getsCredProvider(Context context) {
        if (sCredProvider == null ) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    Constants.COGNITO_POOL_ID,
                    Regions.US_WEST_2);
        }
        return sCredProvider;
    }

    public static AmazonS3Client getsS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getsCredProvider(context.getApplicationContext()));
        }
        return sS3Client;
    }

    public static TransferUtility getsTransferUtility(Context context) {
        if (sTransferUtility == null ) {
            sTransferUtility = new TransferUtility(getsS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }
        return sTransferUtility;
    }
}

package com.ausadhi.mvvm.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.util.Log;

import com.ausadhi.mvvm.data.network.model.ProductModel;


public class ReadExcel {

        public static List<ProductModel> read(Context context) {
            List<ProductModel> list = new ArrayList<ProductModel>();
            ProductModel chave = null;
            String line;
            int i = 0;
            String split[] = null;

            try {
                InputStream is = context.getAssets().open("items.docx");

               // InputStream gzipStream = new GZIPInputStream(is);
                Reader decoder = new InputStreamReader(is);
                BufferedReader buffered = new BufferedReader(decoder);

                while((line = buffered.readLine()) != null) {
                    i++;

                }
                buffered.close();
                Log.d("Items","TOTAL = " + i);
                System.out.println("TOTAL = " + i);
            }
            catch (final IOException ioe) {
                System.err.println("Unhandled exception:");
                ioe.printStackTrace();
                return list;
            }

            return list;
        }

    }
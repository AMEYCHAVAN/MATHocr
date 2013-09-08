package com.datumdroid.android.ocr.simple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.londatiga.android.CropOption;
import net.londatiga.android.CropOptionAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.WriteFile;
import com.googlecode.tesseract.android.TessBaseAPI;

public class SimpleAndroidOCRActivity extends Activity {
	public static final String PACKAGE_NAME = "com.datumdroid.android.ocr.simple";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";

	// You should have the trained data file in assets folder
	// You can get them at:
	// http://code.google.com/p/tesseract-ocr/downloads/list
	public static final String lang = "eng";
	String picturePath = "";
	private static final String TAG = "SimpleAndroidOCR.java";
	String recognizedText="1";
	
	Bitmap tempbit = null;
	Bitmap photo  = null;
	
	protected Button _button;
	// protected ImageView _image;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;

	protected static final String PHOTO_TAKEN = "photo_taken";
	private static int RESULT_LOAD_IMAGE = 123;
	private static int REQUEST_CROP_ICON = 456; // qqqqqqq
	Bitmap openbitmap = null;
	private Uri mImageCaptureUri;
	private ImageView mImageView;
 
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}

		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");
				// GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				// while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				// gin.close();
				out.close();

				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG,
						"Was unable to copy " + lang + " traineddata "
								+ e.toString());
			}
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		_field = (EditText) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.button);
		Button matbutton = (Button) findViewById(R.id.buttonmat);
		matbutton.setOnClickListener(btnListener);

		_path = DATA_PATH + "/ocr.jpg";
		//===========================
		
		
		

		
		
		
		//=================
		
		 
        final String [] items			= new String [] {"Take from camera", "Select from gallery"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { //pick from file
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
		Button button 	= (Button) findViewById(R.id.button);
		mImageView		= (ImageView) findViewById(R.id.imageView1);
		
		button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		
		
		
		
		
		
		
		
		
		//=========================
		
		
		
		
		
		
		
		
		
		
		
		
	}

	  
	private OnClickListener btnListener = new OnClickListener()
	    {  
	

	        public void onClick(View v)
	         {//  Log.v(TAG, "n startintetrnt : "  );
	        	EditText text = (EditText)findViewById(R.id.field);
		        String dat = text.getText().toString();
		        //Log.v(TAG, "n TEXT: " +dat );
	        	
		        
		        Intent intent11 = new Intent(); 
		        intent11.setClass(SimpleAndroidOCRActivity.this , com.datumdroid.android.ocr.simple.mathappengine.class);
		       
		        
		        
		      //  Intent intent11 = new Intent(SimpleAndroidOCRActivity.this,com.datumdroid.android.ocr.simple.mathappengine.class);
	    	 
	    		intent11.putExtra("op", dat);
	    	//	  Log.v(TAG, "n startintetrnt :222222222 "  );
   startActivity(intent11);	
	    Log.v(TAG, "n startintetrnt :444444444444 "  );
	        	
	        if(dat!="")
	        {   Log.v(TAG, "n startintetrnt :444444444444 "  );
	        return;}
	        	
	        	
	         
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	Log.v(TAG, "n TEXT: "  );
                  
	        	 //===========liner parser ==========================================//
	            
	        	String v1[] = new String[10];
	            String noum[] = new String[50];
	            
	        	
	             
	             
	            float a[] = new float[2];  // x coefficients
	            float b[] = new float[2];  // y coefficients
	            float c[] = new float[2];  // constants on right side of equations
	            
	            
	   try{    

	            
	    //  String dat;					  dat ="3x+2y=5 5x-2y=8";
					

						  dat = dat.replace("\n", "@").replace("\r", "@");
						    dat = dat.replace(" ", "@");
						                  System.out.println("-----------------------------"+dat);

						   
	                    
	             int k=0;   
	             int vk=0;
	             String newli="\n";
	          
	         
	             
	             
	                   for (int i = 0; i<50; i++)  // Read the input data file
	                  {  noum[i] ="";
	                       v1[i%9]="";
	                   }
	              
	                   for (int i = 0; i<dat.length(); i++)  // Read the input data file
	                  {
	            	   
	                      System.out.println("-----------------------------"+i);
	                      String q=""+dat.charAt(i);
	                   
	                      
	                        if(q.matches("[a-z]*"))
	                         { if(v1[vk++].equals(q))
	                         {}
	                         else
	                         {v1[vk++]=q;}
	                             //System.out.print("apla");
	                           k++; 
	                             
	                          }
	                      
	                        
	                          if(q.matches("[0123456789]*"))
	                         {
	                              if(noum[k]!=null)
	                         {  noum[k]=  noum[k]+q;
	                           System.out.println("attach"+q+"======"+noum[k]);}
	                            else
	                         {     noum[k]=q;
	                         System.out.println("fno"+q+"======"+noum[k]);
	                         }
	                              
	                  
	                          }
	                          
	                          
	                          if(q.matches("[+-]*"))
	                          {  k++; 
	                            if(q.equals("-"))
	                            {
	                          	  			
	                            			if(noum[k]==null)
	                            					{noum[k]="";}
	                          
	                            					noum[k]=  q+noum[k-1];  
	                        
	                            						System.out.println("asctualnoz"+"======"+noum[k]);
	                             
	                            //  System.out.print("optor");
	                            }
	                           }
	                       
	                       
	                      
	                      
	                           if(q.matches("[=]*"))
	                         { k++; 
	                            // System.out.print("equals");
	                          } 
	                           if(q.matches("[@]*"))
	                           { k++; 
	                              // System.out.print("equals");
	                            }
	                        
	                      
	                           if(newli.equals(q))
	                     {k++; 
	                     System.out.println("asctualnoz"+"======"+noum[k]);
	                      //   System.out.print("poop");
	                     }
	                    
	                   // else        { System.out.println(" " + dat.charAt(i));          }
	                           if(newli.equals(""))                                            	   
	  	                     { 
	  	                     System.out.println("null"+"======"+q);
	  	                      //   System.out.print("poop");
	  	                     }
	                     
	                      
	                   }
	                   
	                   
	                   
	                         
	                   
	                         System.out.println("-----------------------------11");   
	                  
	                  
	                  
	                  
	                Integer actno[] = new Integer[9];
	                int kcounter=0;
                    System.out.println("@@@@@@@@@@@------------------11");   

	                 for (int i = 0; i<=10; i++)  // Read the input data file
	                  {  System.out.println(noum[i]);
	                	 
	                   
	                         if(noum[i]!="")   
	                         {   actno[kcounter]=Integer.parseInt(noum[i]);
	                         kcounter=kcounter+1;
	                         }
	                        
	                 }
                     System.out.println("-----------------------------22");   

	                 
	                 for (int i = 0; i<6; i++)  // Read the input data file
	                  {System.out.println(actno[i]);
	                 }
	                
	                 
	                         System.out.println("----------------------------33");
	                      String test1=v1[1];      
	                      String test2=v1[3];
	                           


	                     //  System.out.println(test1+"hghj"+test2);
	                      ///             System.out.println(test2+"hghj"+test1);

	                  for (int i = 0; i<9; i++)  // Read the input data file
	                  { // System.out.println(v[i]);
	                        
	                	  /*
	                	  if((!v1[i].equals(test1)) && (!v1[i].equals(test2)) && (!v1[i].equals("")) )
	                         {       System.out.println("invalid equation  more than 2 varible ax+by=c");
	     	  	        	Toast.makeText(getApplicationContext(), "invalid equation  more than 2 varible ax+by=c",	Toast.LENGTH_LONG).show();
							
	            				}
	
	                         */ 
	                     
	                 }
 	            //String eol = System.getProperty("line.separator");
	              
	                  
	                               System.out.println("-k---------------------------");

	                     float dd, dx, dy;
	                  // Compute using Cramer's rule
	                  dd = actno[0] * actno[4] - actno[3] * actno[1]; // denominator
	                                         
	                  dx = actno[2] * actno[4] - actno[1] * actno[5]; // x numerator
	                  dy = actno[0] * actno[5] - actno[2] * actno[3]; // y numerator
	                  float x = dx/dd;  // Divide determinants to get answer
	                  float y = dy/dd;

	                  System.out.println(test1 +"= " + x + " "+test2+" = " + y);
	                  
	  	        	Toast.makeText(getApplicationContext(), test1 +"= " + x + " "+test2+" = " + y,	Toast.LENGTH_LONG).show();

	  	          TextView t1=(TextView) findViewById(R.id.textView1);	    
		            t1.setText(  test1 +"= " + x + " "+test2+" = " + y);  
	                 
	   }
	   catch (Exception e) {
		   Toast.makeText(getApplicationContext(), " The format is incorrect -->>use  ax+by=c",	Toast.LENGTH_LONG).show();
	}
	                 
	         //===================================================================//   
	            
	        	Toast.makeText(getApplicationContext(), ">><<",	Toast.LENGTH_LONG).show();
	        } 

	    };  
	 

	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
	    switch (requestCode) {
		    case PICK_FROM_CAMERA:
		    	doCrop();
		    	
		    	break;
		    	
		    case PICK_FROM_FILE: 
		    	mImageCaptureUri = data.getData();
		    	
		    	doCrop();
	    
		    	break;	    	
	    
		    case CROP_FROM_CAMERA:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {	        	
		              photo = extras.getParcelable("data");
		              
		            mImageView.setImageBitmap(photo);
		        	Log.v(TAG, "onphototaken"+photo.getHeight());
		        }
		        
		        
		        onPhotoTaken(); //ocr calling amey with bitmap "photo"
		        
		        
		        
		        File f = new File(mImageCaptureUri.getPath());            
		        
		        if (f.exists()) f.delete();
	
		        break;

	    }
	}
    
    private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(mImageCaptureUri);
            
        //    intent.putExtra("outputX", 200);
        //    intent.putExtra("outputY", 200);
        //    intent.putExtra("aspectX", 1);
       //     intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i 		= new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	        	startActivityForResult(i, CROP_FROM_CAMERA);
        	} else {
		        for (ResolveInfo res : list) {
		        	final CropOption co = new CropOption();
		        	
		        	co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		        	co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		        	co.appIntent= new Intent(intent);
		        	
		        	co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		            cropOptions.add(co);
		        }
	        
		        CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Choose Crop App");
		        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		            @Override
					public void onClick( DialogInterface dialog, int item ) {
		                startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		            }
		        });
	        
		        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		            @Override
		            public void onCancel( DialogInterface dialog ) {
		               
		                if (mImageCaptureUri != null ) {
		                    getContentResolver().delete(mImageCaptureUri, null, null );
		                    mImageCaptureUri = null;
		                }
		            }
		        } );
		        
		        AlertDialog alert = builder.create();
		        
		        alert.show();
		        Log.v(TAG,"");
        	}
        }
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//===========================-----------------------------========================
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken() {
		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;


		//	Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
		Bitmap bitmap = photo;
		Log.v(TAG, "onphototaken"+bitmap.getHeight());

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}

		// _image.setImageBitmap( bitmap );
		Log.v(TAG, _path);
		Log.v(TAG, "Before baseApi");
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setImageBitmap(bitmap);
		// =======================================================================================
		/*
		 * if(!OpenCVLoader.initDebug()) { Log.e(TAG, "initDebugError!"); }
		 * 
		 * Mat plateImage = Highgui.imread(_path);
		 * 
		 * 
		 * Mat gray = new Mat(plateImage.size(),CvType.CV_8UC1);
		 * //Imgproc.cvtColor(plateImage, gray, Imgproc.COLOR_BGR2GRAY);
		 * 
		 * Mat thresh = new Mat(plateImage.size(),CvType.CV_8UC1); //
		 * Imgproc.adaptiveThreshold(gray, thresh, 255, 1, 1, 11, 2);
		 * 
		 * Utils.matToBitmap(thresh, openbitmap); Highgui.imwrite(_path,
		 * thresh); openbitmap=BitmapFactory.decodeFile(_path);
		 * imageView.setImageBitmap(openbitmap);
		 */
		// ==========================================================================
		// ----Leptonica masti-----------\\Pyro
		// ==========================================================================

		// bitmap=convx(bitmap);

		// bitmap=Bitmap.createScaledBitmap(bitmap,
		// (bitmap.getWidth())/2,(bitmap.getHeight())/2, false);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Pix px = ReadFile.readBitmap(bitmap);

		// Pix newb=Convert.convertTo8(px);
		// newb= Binarize.otsuAdaptiveThreshold(px);
		Log.v("ameytest", "converted to 8 bit");

		Pix newb = Binarize.otsuAdaptiveThreshold(px, px.getHeight(),
				px.getWidth(), 0, 0, (float) 0.1);
		// newb=Binarize.sauvolaBinarizeTiled(px, 1,(float) 0.34, 1, 1);
		Log.v("ameytest", "completed otsuAdaptiveThreshold ");

		Bitmap xyz = WriteFile.writeBitmap(newb);
		Log.v("ameytest", "  writeBitmap ");
		xyz = xyz.copy(Bitmap.Config.ARGB_8888, true);

		Log.v("ameytest", "  writeBitmap ARGB_8888");
		imageView.setImageBitmap(xyz);

		// ============================================================================================

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.clear();
		// baseApi.VAR_CHAR_WHITELIST
		baseApi.setDebug(true);

		baseApi.init(DATA_PATH, lang);
		// baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"1234567890ABCDEFGHJKLMNPRSTVWXYZ");
		// baseApi.init(DATA_PATH, lang,2);//amey line better accuracy
		// baseApi.setImage(bitmap);
		// baseApi.setPageSegMode(TessBaseAPI.OEM_CUBE_ONLY);
		baseApi.setPageSegMode(TessBaseAPI.OEM_DEFAULT);
		baseApi.setImage(xyz);

		  recognizedText = baseApi.getUTF8Text();

		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with
		// it.
		// We will display a stripped out trimmed alpha-numeric version of it
		// (if lang is eng)
		// so that garbage doesn't make it to the display.

		Log.v(TAG, "OCRED TEXT: " + recognizedText);
	 	Toast.makeText(getApplicationContext(), ">>" + recognizedText + "<<",
				Toast.LENGTH_LONG).show();
	//	Toast.makeText(getApplicationContext(), ">>" + recognizedText + "<<",
	//			Toast.LENGTH_LONG).show();

		if (lang.equalsIgnoreCase("eng")) {
			//recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}

		recognizedText = recognizedText.trim();

		if (recognizedText.length() != 0) {
			// _field.setText(_field.getText().toString().length() == 0 ?
			// recognizedText : _field.getText() + " " + recognizedText);
			// _field.setSelection(_field.getText().toString().length());
			_field.setText(recognizedText);

		}
		if (recognizedText.length() == 0) {
			Log.v("ameytest", "Failsafemode   ");
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

			imageView.setImageBitmap(bitmap);
			
			TessBaseAPI baseApifail = new TessBaseAPI();
			bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
			imageView.setImageBitmap(bitmap);
			Log.v("ameytest", "Failsafemode" + "set scale");
			baseApifail.clear();
			baseApifail.setDebug(true);

			baseApifail.setPageSegMode(TessBaseAPI.OEM_DEFAULT);

			baseApifail.setImage(bitmap);

			recognizedText = baseApifail.getUTF8Text();
			Log.v("ameytest", "Failsafemode" + recognizedText);
			baseApifail.end();

			_field.setText(recognizedText);
			return;
		}

		// Cycle done.
	}

}
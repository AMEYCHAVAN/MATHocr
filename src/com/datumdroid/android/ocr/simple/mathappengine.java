package com.datumdroid.android.ocr.simple;
 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 

public class mathappengine extends Activity  implements OnClickListener
{
	EditText e1,e2;
	Button b1;
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.appengine);
		e1 = (EditText) findViewById(R.id.editText1);
		  
		  Bundle extras = getIntent().getExtras();

		      String value = extras.getString("op");
		  
		  
		e1.setText(value);
	 
		b1=(Button) findViewById(R.id.buttonmaths);
		b1.setOnClickListener(this);
		//////////////
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		// Intent intent = getIntent();
         
		 String TAG = null;
		 
		 
		 
		 //====================================================
		 String v1[] = new String[10];
         String noum[] = new String[50];
         
     	
          
          
         float a[] = new float[2];  // x coefficients
         float b[] = new float[2];  // y coefficients
         float c[] = new float[2];  // constants on right side of equations
         
         
try{    

         
 //  String dat;					  dat ="3x+2y=5 5x-2y=8";
				
	 EditText e1 = (EditText)findViewById(R.id.editText1);
     String dat=e1.getText().toString();
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

	        	 EditText e2 = (EditText)findViewById(R.id.editText2);
		     
	           e2.setText(  test1 +"= " + x + " "+test2+" = " + y);  
              
}
catch (Exception e) {
	   Toast.makeText(getApplicationContext(), " The format is incorrect -->>use  ax+by=c",	Toast.LENGTH_LONG).show();

       e2.setText( " The format is incorrect -->>use  ax+by=c");  
}
              
		 
		 
		 
		 
		 
		 
		 //===========================================
		   
	 
	
	}
	
	
	
	
	
	
	
	
	
		
}

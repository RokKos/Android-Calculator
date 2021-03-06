package com.example.calculator;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    public WebView mWebView;
    private StringBuilder mMathString;
    private ClickListner GUMB;
    private String ANS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        //naredi nov string
        mMathString = new StringBuilder();

        //Omogoci javascript za  evalvirati izrazov
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);


        //dodeli funkcijo vsem gumbom
        GUMB = new ClickListner();
        int SeznamGumbov[] = { R.id.button0, R.id.button1, R.id.button2,
                R.id.button3, R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9, R.id.LeviZaklepaj,
                R.id.Deljeno, R.id.DesniZaklepaj, R.id.Krat,
                R.id.Minus, R.id.Pika, R.id.ZbrisiVse,
                R.id.ZbrisiZnak, R.id.JeEnako, R.id.Plus,
                //R.id.sin, R.id.cos
        };
        for (int id : SeznamGumbov){
            View v = findViewById(id);
            v.setOnClickListener(GUMB);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /*private int vsota = 0; //dostopna samo v tem activityu
    public void Racunaj(View v){
        Button Gumb = (Button) v;
        String strStevilo = (String) Gumb.getText();
        int stevilo = Integer.parseInt(strStevilo);
        vsota += stevilo;
        TextView izpis = (TextView) findViewById(R.id.textView);
        izpis.setText(Integer.toString(vsota));


    }*/


    private class ClickListner implements View.OnClickListener{
        boolean f = true;
        @Override
        public void onClick(View v){
            switch(v.getId()){
                //Zbrise en znak
                case R.id.ZbrisiZnak:
                    if(mMathString.length()>0){
                        mMathString.deleteCharAt(mMathString.length()-1);
                        updateWebView(false);
                        if(mMathString.length()==0){
                            mWebView.loadData("","text/html", "null");
                        }
                    }
                    break;
                //Zbrise vse
                case R.id.ZbrisiVse:
                    if(mMathString.length()>0){
                        mMathString.delete(0, mMathString.length());
                        mWebView.loadData("","text/html", "null"); // ko vse zbrise ne evalvira ker drugace pride undefined
                    }
                    break;
                case R.id.JeEnako:
                    updateWebView(true);
                    break;
                //case R.id.sin:
                //    mMathString.append(((Button) v).getText());
                //Drugace pa doda znak
                default:
                    mMathString.append(((Button) v).getText());
                    updateWebView(false); //samo osvezi input ne pa output
            }

        }

    }

    private void updateWebView(boolean show) {

        StringBuilder builder = new StringBuilder();

        builder.append("<html><body>");
        builder.append("<script type=\"text/javascript\">document.write('");
        builder.append(mMathString.toString()); //izraz
        builder.append("');");
        if (show==true){
            builder.append("document.write('<br />=' + eval(\""); //Evalvira
            builder.append(mMathString.toString());//resen izraz
            builder.append("\"));");
        }
        builder.append("</script>");
        builder.append("</body></html>");
        mWebView.loadData(builder.toString(), "text/html", "null");
        ANS = builder.toString();
    }

}

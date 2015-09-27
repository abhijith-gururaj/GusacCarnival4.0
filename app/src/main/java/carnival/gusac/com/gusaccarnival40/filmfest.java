package carnival.gusac.com.gusaccarnival40;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class filmfest extends Fragment {
    public filmfest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Welcome) getActivity()).getSupportActionBar().hide();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_filmfest, container, false);
        WebView webView= (WebView) view.findViewById(R.id.filmfest_webView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("http://gitamstudio.com");

        return view;
    }
}

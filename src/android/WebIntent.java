package mobi.monaca.framework.plugin;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import mobi.monaca.framework.plugin.MyFileProvider;

// import android.support.v4.content.FileProvider;

/**
 * WebIntent is a PhoneGap plugin that bridges Android intents and web
 * applications:
 *
 * 1. web apps can spawn intents that call native Android applications. 2.
 * (after setting up correct intent filters for PhoneGap applications), Android
 * intents can be handled by PhoneGap web applications.
 *
 * @author boris@borismus.com
 *
 */
public class WebIntent extends CordovaPlugin {

    private CallbackContext callbackContext = null;
    
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        this.callbackContext = callbackContext;
        
        if (action.equals("startActivity")) {
            if (args.length() != 1) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            }

            // Parse the arguments
            JSONObject obj = args.getJSONObject(0);
            String type = obj.has("type") ? obj.getString("type") : null;
            Uri uri = obj.has("url") ? Uri.parse(obj.getString("url")) : null;
            JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
            Map<String, String> extrasMap = new HashMap<String, String>();

            // Populate the extras if any exist
            if (extras != null) {
                JSONArray extraNames = extras.names();
                for (int i = 0; i < extraNames.length(); i++) {
                    String key = extraNames.getString(i);
                    String value = extras.getString(key);
                    extrasMap.put(key, value);
                }
            }

            startActivity(obj.getString("action"), uri, type, extrasMap);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            
        } else if (action.equals("hasExtra")) {
            if (args.length() != 1) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));                
            }
            Intent i = this.cordova.getActivity().getIntent();
            String extraName = args.getString(0);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName)));

        } else if (action.equals("getExtra")) {
            if (args.length() != 1) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            }
            Intent i = this.cordova.getActivity().getIntent();
            String extraName = args.getString(0);
            if (i.hasExtra(extraName)) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, i.getStringExtra(extraName)));
            } else {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
            }
        } else if (action.equals("getUri")) {
            if (args.length() != 0) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            }

            Intent i = this.cordova.getActivity().getIntent();
            String uri = i.getDataString();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, uri));
        } else if (action.equals("onNewIntent")) {
            if (args.length() != 0) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            }

            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        } else if (action.equals("sendBroadcast"))
        {
            if (args.length() != 1) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            }

            // Parse the arguments
            JSONObject obj = args.getJSONObject(0);

            JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
            Map<String, String> extrasMap = new HashMap<String, String>();

            // Populate the extras if any exist
            if (extras != null) {
                JSONArray extraNames = extras.names();
                for (int i = 0; i < extraNames.length(); i++) {
                    String key = extraNames.getString(i);
                    String value = extras.getString(key);
                    extrasMap.put(key, value);
                }
            }

            sendBroadcast(obj.getString("action"), extrasMap);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        }        
        else {
            return false;
        }
        return true;
    }
    
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action
     *            The action to execute.
     * @param args
     *            JSONArray of arguments for the plugin.
     * @param callbackId
     *            The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        try {
            if (action.equals("startActivity")) {
                if (args.length() != 1) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }

                // Parse the arguments
                JSONObject obj = args.getJSONObject(0);
                String type = obj.has("type") ? obj.getString("type") : null;
                Uri uri = obj.has("url") ? Uri.parse(obj.getString("url")) : null;
                JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
                Map<String, String> extrasMap = new HashMap<String, String>();

                // Populate the extras if any exist
                if (extras != null) {
                    JSONArray extraNames = extras.names();
                    for (int i = 0; i < extraNames.length(); i++) {
                        String key = extraNames.getString(i);
                        String value = extras.getString(key);
                        extrasMap.put(key, value);
                    }
                }

                startActivity(obj.getString("action"), uri, type, extrasMap);
                return new PluginResult(PluginResult.Status.OK);

            } else if (action.equals("hasExtra")) {
                if (args.length() != 1) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }
                Intent i = this.cordova.getActivity().getIntent();
                String extraName = args.getString(0);
                return new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName));

            } else if (action.equals("getExtra")) {
                if (args.length() != 1) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }
                Intent i = this.cordova.getActivity().getIntent();
                String extraName = args.getString(0);
                if (i.hasExtra(extraName)) {
                    return new PluginResult(PluginResult.Status.OK, i.getStringExtra(extraName));
                } else {
                    return new PluginResult(PluginResult.Status.ERROR);
                }
            } else if (action.equals("getUri")) {
                if (args.length() != 0) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }

                Intent i = this.cordova.getActivity().getIntent();
                String uri = i.getDataString();
                return new PluginResult(PluginResult.Status.OK, uri);
            } else if (action.equals("onNewIntent")) {
                if (args.length() != 0) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                return result;
            } else if (action.equals("sendBroadcast"))
            {
                if (args.length() != 1) {
                    return new PluginResult(PluginResult.Status.INVALID_ACTION);
                }

                // Parse the arguments
                JSONObject obj = args.getJSONObject(0);

                JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
                Map<String, String> extrasMap = new HashMap<String, String>();

                // Populate the extras if any exist
                if (extras != null) {
                    JSONArray extraNames = extras.names();
                    for (int i = 0; i < extraNames.length(); i++) {
                        String key = extraNames.getString(i);
                        String value = extras.getString(key);
                        extrasMap.put(key, value);
                    }
                }

                sendBroadcast(obj.getString("action"), extrasMap);
                return new PluginResult(PluginResult.Status.OK);
            }
            return new PluginResult(PluginResult.Status.INVALID_ACTION);
        } catch (JSONException e) {
            e.printStackTrace();
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (this.callbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
            result.setKeepCallback(true);
            this.callbackContext.sendPluginResult(result);
        }
    }

    void startActivity(String action, Uri uri, String type, Map<String, String> extras) {
        
        // For Android 7.0
        // add FileProvider
        // if (type.equals("application/pdf")) {
            MyFileProvider myFileProvider = new MyFileProvider();
            File file = new File(uri.toString().replace("content://", ""));
            // Log.e("FileProvider", "extras:" + extras + "  URI:" + uri.toString() +"  file.exists:" + file.exists());
            uri = myFileProvider.getUriForFile(this.cordova.getActivity().getApplicationContext(),
                                               this.cordova.getActivity().getApplicationContext().getPackageName(), file);
        // }
        
        Intent i = (uri != null ? new Intent(action, uri) : new Intent(action));
        
        if (type != null && uri != null) {
            i.setDataAndType(uri, type); //Fix the crash problem with android 2.3.6
            
            // if (type.equals("application/pdf")) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // For Android 7.0
            // }
        } else {
            if (type != null) {
                i.setType(type);
            }
        }
        
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            // If type is text html, the extra text must sent as HTML
            if (key.equals(Intent.EXTRA_TEXT) && type.equals("text/html")) {
                i.putExtra(key, Html.fromHtml(value));
            } else if (key.equals(Intent.EXTRA_STREAM)) {
                // allowes sharing of images as attachments.
                // value in this case should be a URI of a file
                i.putExtra(key, Uri.parse(value));
            } else if (key.equals(Intent.EXTRA_EMAIL)) {
                // allows to add the email address of the receiver
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { value });
            } else {
                i.putExtra(key, value);
            }
        }
        this.cordova.getActivity().startActivity(i);
    }

    void sendBroadcast(String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            intent.putExtra(key, value);
        }

        this.cordova.getActivity().sendBroadcast(intent);
    }

}
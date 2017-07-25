# AnimatedLoadingIndicator
AnimatedLoadingIndicator is a animated Progress Dialog Which you can easily implemented in your Android Code.

## Demo
![AnimatedLoadingIndicator](screenshots/AnimatedLoadingIndicator.gif)

   ## Usage
   ### Step 1 : Add "AnimatedLoadingIndicator" to your Android project.

   1- Open your project in Android Studio.
   2- Download the library
       (using Git Link ---> https://github.com/yash786agg/AnimatedLoadingIndicator.git)
                                        or 
       (Download a zip File archive to unzip)
    
   3- Create a folder "AnimatedLoadingIndicator" in your project.
   4- Copy and paste the Code to your AnimatedLoadingIndicator folder
   5- On the root of your project directory create/modify the settings.gradle file. It should contain something like the following:

      include 'MyApp', ':AnimatedLoadingIndicator'

   6- Go to File > Project Structure > Modules.
   7- App > Dependencies.
   8- Click on the more on the left green "+" button > Module dependency.
   9- Select "AnimatedLoadingIndicator Library".
   
   ### Step 2 : Add Code to your Project
   
        Dialog progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        progressDialog.show();
        
        /*
         * To Dismiss or Cancel the ProgressDialog use.
         *
         *  if(progressDialog != null)
                        {
                            progressDialog.cancel();
                            progressDialog.hide();
                        }
          */

package za.co.lbnkosi.firebase_login.base;

public interface Base {
    void onError(String message);

    void showLoadingScreen();

    void hideLoadingScreen();
}

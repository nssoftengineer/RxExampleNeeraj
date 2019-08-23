package rxexample.sra.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
 
public class Example3FilterActivity extends AppCompatActivity {
 
    /**
     * Basic Observable, Observer, Subscriber example
     * Observable emits list of animal names
     * You can see Disposable introduced in this example
     */
    private static final String TAG = Example3FilterActivity.class.getSimpleName();
 
    private Disposable disposable;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        // observable
        Observable<String> animalsObservable = getAnimalsObservable();
 
        // observer
        Observer<String> animalsObserver = getAnimalsObserver();
 
        // observer subscribing to observable
        animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                   @Override
                   public boolean test(String s) throws Exception {
                       return s.toLowerCase().startsWith("b");
                   }
               })
                .subscribeWith(animalsObserver);
    }
 
    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {
 
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }
 
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }
 
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
 
            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }
 
    private Observable<String> getAnimalsObservable() {
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
 
        // don't send events once the activity is destroyed
        disposable.dispose();
    }
}
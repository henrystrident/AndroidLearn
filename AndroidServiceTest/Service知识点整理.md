# 目录
- [服务是什么](#服务是什么)
- [线程](#线程)
	- [线程的基本使用](#线程的基本使用)
	- [子线程更新UI](#子线程更新UI)
- [服务的基本用法](#服务的基本用法)


# [服务是什么](#目录)
- 服务是实现程序在后台运行的解决方案，如果有需要长期执行并且不需要和用户进行交互的任务，那么服务是其最好的实现方法。但是，服务并不是独立的进程，它还是依赖于应用程序的进程，并且服务自己不会开启线程，所有代码都是默认运行在主线程中的，所以我们需要在服务内部创建子线程，并执行具体任务。所以我们记下来先学习一下线程的知识。




# [线程](#目录)
- 如果我们想执行类似网络请求等操作，通常是放在线程中完成的，因为如果服务器不是立刻相应我们的请求，而我们又没有将其放在子线程中，那么很有可能造成线程阻塞。

## [线程的基本使用](#目录)
- 启动线程的方法有很多，比如定义一个自己的线程类，但是这种方法==耦合性==很高，所以我们选择实现一个接口，并在线程中运行，如：

```java
class MyThread implements Runnable()
{
	@ Override
	public void run()
	{
		//具体逻辑
	}
}

MyThread myThread = new MyThread();
new Thread(myThread).start();
```

## [子线程更新UI](#目录)
- 在Android中子线程实际上是不能更新UI的，因为UI是线程不安全的，但是有些时候我们又必须通过子线程对UI进行一定的调整，所以我们可以通过异步消息处理来实现这个功能。在Android中，线程一共有4个重要的对象：
	- **1. Message**
	Message是线程之间进行通信的消息，主要有 `.what` 和 `.obj` 两个对象，前者可以用于进行信号的标识，后者用于携带一些数据。
	- **2. Handler**
	handler是各个线程处理和收发消息的对象，向某个线程发送Message就使用 `handler.sendMessage()` 方法。而发出的消息经过一系列辗转处理之后也会回到Handler的 `handlerMessage()` 方法中。
	- **3. MessageQueue**
	MessageQueue是每个线程存放Message的地方，每个线程有且只有一个MessageQueue。
	- **4. Looper**
	Looper是每个线程中管理 `MessageQueue` 的对象，如果MessageQueue中有Message，Looper就会将消息取出，发送给 `handlerMessage()` 进行处理。

- 接下来，我们通过一个实例来实现子线程更新UI，总体思路是通过按钮点击事件，让子线程给主线程发送消息，主线程接到消息后，就对UI进行更新。

```java
private Button changeText;
private TextView textView;

private static final int CHANGE_TEXT = 1;

private Handler handler = new Handler()
{
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what)
        {
            case CHANGE_TEXT:
                textView.setText("have been changed");
                break;
            default:
                break;
        }
    }
};
```

```java
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    changeText = findViewById(R.id.changeText);
    textView = findViewById(R.id.textView);

    changeText.setOnClickListener(this);
}

@Override
public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.changeText:
            Message message = new Message();
            message.what = CHANGE_TEXT;
            handler.sendMessage(message);
            break;
        default:
            break;
    }
}
```

<img src="./img/1.png" style="zoom:67%;" /><img src="./img/2.png" style="zoom:67%;" />



# [服务的基本用法](#目录)

## 定义和启动服务

- 接下来我们看一下服务的基本用法，首先我们需要定义一个自己的服务，叫做 `MyService`。服务属于Android的四大组件，都要在 `AndroidManifest.xml` 中进行注册，由于我们用AndroidStudio进行声明的，所以已经注册完成了。
- 新建服务后我们需要重写三个较为常见的方法，`onCreate()`，`onStartCommand()`，`onDestroy()`。分别代表服务创建，启动和销毁时应当做的操作，这里我们可以在日志中打印这些过程。

```java
@Override
public void onCreate() {
    super.onCreate();
    Log.d("MyService","创建");
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("MyService", "开启");
    return super.onStartCommand(intent, flags, startId);
}

@Override
public void onDestroy() {
    Log.d("MyService","销毁");
    super.onDestroy();
}
```

- 接着，我们通过按钮点击事件来看看服务的整个过程，由于服务是依赖于Activity存在的，所以启动和关闭服务都需要通过Intent来完成，但是与启动一个Activity不同，服务的开启和关闭是通过 `startService()` 和 `stopService()` 来完成的。

```java
public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.startService:
            Intent startIntent = new Intent(ServiceTest.this, MyService.class);
            startService(startIntent);
            break;
        case R.id.stopService:
            Intent stopIntent = new Intent(ServiceTest.this, MyService.class);
            stopService(stopIntent);
            break;
        default:
            break;
    }
}
```

<img src="./img/3.png"  />

## 活动和服务进行通信
- 虽然刚刚我们启动了一个服务，但是一旦服务启动了，就和我们的Activity没什么关系了，相当于Activity只是允许服务进行启动，现在我们想让活动和服务关系更加紧密，能通过活动对服务进行一些控制，那么现在我们需要借助 `MyService` 中的 `onBind()` 绑定方法了。首先我们要在 `Myservice` 中定义一个自己的Binder类，并在其中定义一些功能，比如 `startDownload()` 和 `getProgress()`。

```java
private DownloadBinder mBinder;
    
class DownloadBinder extends Binder
{
    public void startDownload()
    {
        Log.d("MyService", "开始下载");
    }

    public void getProgress()
    {
        Log.d("MyService", "下载进度:50%");
    }
}

...

@Override
public IBinder onBind(Intent intent) {
    return mBinder;
}
```

- 接着，我们需要在活动中去调用这些方法。首先我们要将活动与服务绑定，实现这个功能我们需要一个 `ServiceConnection` 对象，并且在初始化这个对象时我们就可以指出一旦活动与服务绑定，就执行下载和读取进度操作。

```java
private MyService.DownloadBinder downloadBinder;
private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        downloadBinder = (MyService.DownloadBinder) service;
        downloadBinder.startDownload();
        downloadBinder.getProgress();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
};
```

- 最后我们通过点击事件完成活动与事件的绑定与解除，这就要用到 `bindService()` 和 `unbindService()` 方法了。前者传入三个参数intent，connection和一个标志位，后者只需要传入一个connection即可。

```java
case R.id.bindService:
	Intent bindService = new Intent(ServiceTest.this, MyService.class);
	bindService(bindService, connection, BIND_AUTO_CREATE);
	break;
case R.id.unbindService:
	unbindService(connection);
```

![](./img/4.png)
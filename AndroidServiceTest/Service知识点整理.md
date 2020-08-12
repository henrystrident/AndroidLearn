# 目录
- [服务是什么](#服务是什么)
- [线程](#线程)
	- [线程的基本使用](#线程的基本使用)
	- [子线程更新UI](#子线程更新UI)


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

<img src="\img\1.png" style="zoom: 50%;" />    <img src="\img\2.png" style="zoom:50%;"/>


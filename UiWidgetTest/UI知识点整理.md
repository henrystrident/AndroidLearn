# UI知识点整理
[toc]
# [一. TextView](#UI知识点整理)
## 对齐方式
### 文字对齐
- 表示文字在textview内部的对齐方式，如居中：`android:layout="center"`



# [二. Button](#UI知识点整理)
## 点击事件设定
- 点击事件是通过button对象调用.setOnclickListener()方法实现的，该方法需要传入一个View.OnclickListener对象。如自定义一个addProgress类，实现progressBar的增加进度功能
```java
class addProgress implements View.OnClickListener
{
	@Override
    public void onClick(View v) {
    	int progress = progressBar.getProgress();
        progress += 10;
        progressBar.setProgress(progress);
    }
}
```



# [三. Editext](#UI知识点整理)
## 提示性文字
```java
android:hint="Type something here"
```
## 最大行数
当不想让EditText显示全部内容时，可以设置一个EditText中最多有多少行内容：
```java
android:maxLines="2"
```



# [四. ProgressBar](#UI知识点整理)
- progressBar是一个进度条，可以是环状，也可以是条状，样式很多。

## 运行效果
- 环状ProgressBar有三种可见性：可见，不可见及消失，分别用
```java
.setVisbility(View.VISIBLE)
.setVisbility(View.INVISIBLE)
.setVisbility(View.GONE)
```
表示。后两者的区别在于，INVISBLE仅仅是不可见，但是控件仍然占据相对应的位置，而GONE代表控件不占据任何位置。

- 条状ProgressBar可以通过`android:max="100"`设置进度条的最大值。并且通过点击按钮的形式修改进度条，代码放在[code](# 点击事件设定)。



# [五. AlertDialog](# UI知识点整理)
## 作用

- AlertDialog是一个在当前界面弹出的对话框，可以屏蔽与其他控件交互，所以通常用作非常重要的信息的提示。

## 创建
- AlertDialog通过AlertDialog.builder方法创建实例，然后设置标题，信息，以及是否可以取消。接着调用 `.setPostiveButton()` 和 `.setNegtiveButton()` 设置确认和取消按钮对应的事件。完成以上步骤后通过 `.show()` 方法显示AlertDialog。
```java
AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
dialog.setTitle("测试");
dialog.setMessage("something important");
dialog.setCancelable(false);
dialog.setPositiveButton("OK", new positive());
dialog.setNegativeButton("CANCEL", new cancel());
dialog.show();
```
```java
class positive implements DialogInterface.OnClickListener
{
	@Override
    public void onClick(DialogInterface dialog, int which) 
    {
    	Toast.makeText(MainActivity.this, "OK", 					              Toast.LENGTH_SHORT).show();
    }
}

class cancel implements DialogInterface.OnClickListener
{
	@Override
    public void onClick(DialogInterface dialog, int which) 
    {
    	Toast.makeText(MainActivity.this, "CANCEL", 								Toast.LENGTH_SHORT).show();
    }
}
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200803212139202.png" alt="image-20200803212139202" style="zoom:67%;" />



# [六. ProgressDialog](# UI知识点整理)
- ProgressDialog是以对话框形式出现的进度条，通常用于加载时间较长时使用，可以设置为在加载时不能与其他控件进行交互。
```java
ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
progressDialog.setTitle("测试");
progressDialog.setMessage("Running");
progressDialog.setCancelable(true);
progressDialog.show();
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200803214100045.png" alt="image-20200803214100045" style="zoom:67%;" />



# [七.自定义控件](# UI知识点整理)
## 布局
- 很多app都在顶部定义自己的标题栏(actionBar)，如左上角返回键，右上角其他功能键等。这一节，我们自定义一个标题栏，由两个Button和一个TextView组成，这个布局需要重新用一个.xml文件实现，命名为title.xml。

  ![image-20200803214628925](C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200803214628925.png)
  
## 引用
- 之后在actiivty_main.xml中引用这个布局，只需将下面这段代码放在布局文件的最前面即可。
```java
<include layout="@layout/title"/>
```

- 引用自己的actionBar需要将系统自带的actionBar屏蔽掉
```java
ActionBar actionBar = getSupportActionBar();
if (actionBar != null)
{
	actionBar.hide();
}
```

## 创建自定义的控件
- 上面的实现方法需要在每个页面都对两个按钮单独设置点击事件，如果引用这个控件的页面较多，就会非常麻烦，并且造成代码的重复。此时，我们需要创建自定义的控件。
- 首先我们需要自定义一个名叫TitleLayout的类，即布局，继承自ConstraintLayout(Android新的布局方式，可以完美代替从前的布局，不再会由层层嵌套，提高了效率)，这样就可以在这个类文件中对两个按钮设置点击事件。
- 创建这个类之后首先需要找到对应的布局文件title.xml，由于这个类并不是一个Activity，所以需要用LayoutInflater类来设置布局文件。之后设置按钮点击事件
```java
public class TitleLayout extends ConstraintLayout
{

    private Button back;
    private Button edit;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);

        initView();

        back.setOnClickListener(new Back());

        edit.setOnClickListener(new Edit());
    }

    public void initView()
    {
        back = findViewById(R.id.backButton);
        edit = findViewById(R.id.editButton);
    }

    class Back implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            ((Activity) getContext()).finish();
        }
    }

    class Edit implements View.OnClickListener
    {
    	@Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), recyclerViewTest.class);
            getContext().startActivity(intent);
        }
    }
}
```
- 最后在activity_main.xml中调用这个控件，目前这个控件已经是一个和Button，TextView一样的控件了，所以调用方式和他们一样。
```java
<com.example.uiwidgettest.TitleLayout
android:layout_width="match_parent"
android:layout_height="wrap_content"
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintTop_toTopOf="parent"
app:layout_constraintVertical_bias="0.0" />
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200803221601389.png" alt="image-20200803221601389" style="zoom:67%;" />



# [八.ListView](# UI知识点整理)
- 目前，使用自定义ListView的情况居多，本节以如何创建一个自己的ListView为例，描
  述创建listView的全过程。
## 数据类
- 首先定义自己的数据类型，比如本次定义水果数据类，包含名称和价格，分别是String和Int类型，其中getName()和getPrice()方法一定要写，用于在列表中获取数据。
```java
public class Fruit {

    private String name;
    private int price;
    public Fruit(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName()
    {
        return this.name;
    }

    public int getPrice()
    {
        return this.price;
    }
}
```

## 自定义布局
- ListView中每个item的外观也需要自己定义，由于Fruit类的两个数据元素都是文本类型，所以设置两个TextView即可。

![image-20200804104419622](C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200804104419622.png)

## 自定义Adapter
- Adapter是一个数据适配器，每个ListView都需要设置Adapter，自定义的Adapter继承自ArrayAdapter，通过Adapter才能将布局和数据联系到一起。最简单的Adapter重构方法不再赘述，本文展示最优化的Adapter，优化内容主要是快速滑动时数据的载入速度。

- 首先写构造函数，这里传入三个参数：`Context context`，`int textViewResourceId`，`List<自己的数据类> object`，这三个参数都在实现父类的构造函数中使用。`textViewResourceId` 传入对应的布局文件，在接下来的过程中使用。

- 接着定义一个ViewHolder，用于保存当前视图中的各个控件，这样每次加载时只要从缓存中读取，不用再从布局文件中读。
```java
public class FruitAdapter extends ArrayAdapter<Fruit>
{
    private int resourceId;
    FruitAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Fruit> objects)
    {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    public static class ViewHolder
    {
        TextView name;
        TextView price;
    }
}
```

- 最后是getView()方法，该方法返回一个View类型的数据，也是整个Adapter的重点，在这里实现控件设置数据，以及缓存的保存过程。
- getView()方法接收三个参数：`int position`，`View convertView`，`ViewGroup parent`。`position` 代表ListView的第几个item，`convertView`代表缓存中的view，`parent` 代表父布局。
- 首先定义两个参数，`View view`和`ViewHolder viewHolder`，第一个参数用于读取缓存中的view，并且当作最后返回的数据，如果缓存中没有保存的视图，则现场进行读取；第二个参数用于读取缓存中的控件。通过这两个参数可以发现，该方法优先从缓存中读取布局和控件，避免运行时读取，提高了app运行时的速度。
- 如果缓存中没有布局文件，即 `convertView==null`时，通过 `textViewResourceId`读取布局文件，并将控件读入到 `viewHolder`中，接着用 `view.setTag(viewHolder)`方法，绑定布局及控件。
- 如果缓存中有布局文件，即 `convertView!=null`，则使用 `view.getTag()`方法将对应的控件取出，并赋给 `viewHolder`。
- 最后，分别两个textView分别设置数据，并返回 `view`数据，完成整个过程。
```java
@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
{
	Fruit fruit = getItem(position);
	View view;
	ViewHolder viewHolder;
	if (convertView == null)
    {
        view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.name = view.findViewById(R.id.fruitName);
        viewHolder.price = view.findViewById(R.id.fruitPrice);
        view.setTag(viewHolder);
    }
    else
    {
        view = convertView;
        viewHolder = (ViewHolder) view.getTag();
    }


    assert fruit != null;
    viewHolder.name.setText(fruit.getName());
    viewHolder.price.setText(String.valueOf(fruit.getPrice()));
    return view;

}
```

## 点击事件
- ListView中每个item都可以设置点击事件，与Button的点击事件一样，先定义一个自己的点击事件类，继承自 `AdapterView.OnItemClickListener`，然后通过获取点击位置，设置触发事件，这里设置的出发事件是显示名称及价格。
```java
class showInformation implements AdapterView.OnItemClickListener
{
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Fruit fruit = fruits.get(position);
    Toast.makeText(ListViewTest.this, fruit.getName()+" "+fruit.getPrice(), Toast.LENGTH_SHORT).show();
	}
}
```

```java
listView.setOnItemClickListener(new showInformation());
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200804114649614.png" alt="image-20200804114649614" style="zoom:67%;" />



# [九.RecyclerView](# UI知识点整理)
- RecyclerView是一个比ListView更为强大的滚动控件，可以实现横向滚动，瀑布流等ListView无法实现的效果。目前，RecyclerView正在取代ListView成为主流滚动控件。
## 引入
- RecyclerView属于新增控件，所以使用时首先要在build.gradle文件中添加依赖：

```java
implementation 'androidx.recyclerview:recyclerview:1.1.0'
```

- 在xml文件中引入RecyclerView时，需要将它的完整包路径写出来：

```java
<androidx.recyclerview.widget.RecyclerView
        android:id="@+id/fruitRecyclerVIew"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

## 自定义Adapter
- 与ListView一样，RecyclerView也需要对应的数据类和布局文件，这里我们采用和 [第八节](# 八.ListView)中一样的数据类和布局文件。
- 我们同样需要为RecyclerView建立一个Adapter，`FruitRecyclerViewAdapter`，这个类继承自 `RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder>`。其中<>内的内容是我们需要进行自定义的。
- 首先我们需要一个ViewHolder，用于储存控件，继承自RecyclerView.ViewHolder，它的构造函数以 `View view`为参数，作为找到控件的依据。

```java
static class ViewHolder extends RecyclerView.ViewHolder
{
    TextView name;
    TextView price;

	public ViewHolder(@NonNull View itemView) {
    	super(itemView);
        name = itemView.findViewById(R.id.fruitNameHorizontal);
        price = itemView.findViewById(R.id.fruitPriceHorizontal);
    }
}
```

- 接下来是 `FruitRecyclerViewAdapter`的构造函数，这个构造函数只有一个作用：传入数据列表，并将数据赋给类内的数据类别。

```java
private List<Fruit> FruitList;
FruitRecyclerViewAdapter(List<Fruit> fruitList)
{
	this.FruitList = fruitList;
}
```

- 最后是三个必须重构的函数， `ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)`，`void onBindViewHolder(ViewHolder holder, int position)`，`int getItemCount()`。第一个函数通过LayoutInflater.inflate方法找到布局文件，并将视图与ViewHolder绑定；第二个函数通过position参数确定数据列表中具体的item，并获取其中数据，显示在屏幕上；第三个函数返回数据列表长度。

```java
	@NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item_horizontal, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fruit fruit = this.FruitList.get(position);
        holder.name.setText(fruit.getName());
        holder.price.setText(String.valueOf(fruit.getPrice()));
    }

    @Override
    public int getItemCount() {
        return this.FruitList.size();
    }
```

## 横向排布
作为更加强大的“ListView”，RecyclerView可以实现横向排布。与ListView不同的是，RecyclerView的布局不仅可以在xml文件中设置，还 **必须** 通过LinearLayoutManager对象设置。如果要实现横向排布，则通过 `LinearLayoutManager.setOrientation()`实现。

```java
	RecyclerView recyclerView = findViewById(R.id.fruitRecyclerVIew);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerView.setLayoutManager(layoutManager);
    FruitRecyclerViewAdapter fruitRecyclerViewAdapter = new FruitRecyclerViewAdapter(getData());
    recyclerView.setAdapter(fruitRecyclerViewAdapter);
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200804155148828.png" alt="image-20200804155148828" style="zoom:67%;" />

## 点击事件
- 在RecycleView中，并没有单独的 `setOnItemClickListener()` 方法能直接调用，需要我们在自定义Adapter时设置点击事件，这样做的好处是内部的按钮都能够单独设置点击事件。点击事件在 `OncreateView()` 方法中实现：

```java
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item_horizontal, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = FruitList.get(position);
                Toast.makeText(v.getContext(), fruit.getName()+" "+fruit.getPrice(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }
```

<img src="C:\Users\pgj\AppData\Roaming\Typora\typora-user-images\image-20200804160759528.png" alt="image-20200804160759528" style="zoom:67%;" />
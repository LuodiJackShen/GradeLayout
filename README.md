GradeLayout
-------
[ ![Download](https://api.bintray.com/packages/luodijackshen/gradelayout-maven/GradeLayout/images/download.svg) ](https://bintray.com/luodijackshen/gradelayout-maven/GradeLayout/_latestVersion)

效果图
-------
![效果图](https://github.com/LuodiJackShen/GradeLayout/blob/master/image/demo1.gif)
![效果图](https://github.com/LuodiJackShen/GradeLayout/blob/master/image/demo3.gif)
![效果图](https://github.com/LuodiJackShen/GradeLayout/blob/master/image/demo4.gif)

目前支持更改的属性
-------

```xml
        <!--刻度被选中时候的颜色-->
        <attr name="grade_color_chosen" format="color" />
        <!--刻度未被选中时候的颜色-->
        <attr name="grade_color_unchosen" format="color" />
        <!--刻度被选中后刻度的图标-->
        <attr name="grade_ico_chosen" format="reference" />
        <!--刻度未被选中时刻度的图标-->
        <attr name="grade_ico_unchosen" format="reference" />
        <!--刻度图标的宽高-->
        <attr name="grade_ico_size" format="dimension" />
        <!--刻度文字大小-->
        <attr name="grade_text_size" format="dimension" />
        <!--导航线未被选中部分的颜色-->
        <attr name="nav_line_unchosen_color" format="color" />
        <!--导航线被选中部分的颜色-->
        <attr name="nav_line_chosen_color" format="color" />
        <!--导航button的背景图片-->
        <attr name="nav_button_ico" format="reference" />
        <!--导航button的宽高-->
        <attr name="nav_button_size" format="dimension" />
        <!--刻度的数量-->
        <attr name="max_grade" format="integer" />
        <!--刻度图标和导航线之间的距离-->
        <attr name="gap" format="dimension" />
        <!--刻度图标和刻度文字之间的距离-->
        <attr name="grade_ico_padding" format="dimension" />
        <!--导航线被选中部分的宽度-->
        <attr name="nav_line_chosen_width" format="dimension" />
        <!--导航线未被选中部分的宽度-->
        <attr name="nav_line_unchosen_width" format="dimension" />
		<!--是否自动对齐到刻度，即是否将分数类型从int变成double，默认是true-->
        <attr name="auto_align" format="boolean" />
```

使用
-------
1.  加入下面的依赖
```xml
        compile 'jack.view:gradelayout:1.0.2'
```
      
2.  在布局文件里引入
```xml
        <jack.view.GradeLayout
            android:id="@+id/grade_ly"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="20dp"
            android:layout_marginRight="20dp"
            app:max_grade="3"
            app:...其他属性/>
```

最后
-------
欢迎在issue中批评指教，欢迎star，欢迎访问 <a href="http://blog.csdn.net/a199581" title="我的博客">我的博客</a>

License
-------

    Copyright 2018 Luodi Jack Shen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



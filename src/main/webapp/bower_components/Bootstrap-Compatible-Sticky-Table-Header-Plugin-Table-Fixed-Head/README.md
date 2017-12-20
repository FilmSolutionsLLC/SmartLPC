# jQuery Plug-In: Table fixed head
This jQuery plug-in makes the *&lt;thead&gt;* HTML element fixed on the top of a table if you scroll down.

### Installation

Include script *after* the jQuery library (unless you are packaging scripts somehow else):
```html
<script src="jquery.michiweber.table-head-fixed.min.js"></script>
```

Add the class **table-fixed-head** to your table you want the *&lt;thead&gt;* to be fixed on scrolling.

### Manual use

If you don't want to make use of the automated system you can also call the plugin directly from any *&lt;script&gt;*:

``` javascript
$('table').tfh([options]);
```

### Options

Option | Default value | Default automated value | Description
------------ | ------------- | ------------- | -------------
toggle | 0 | 0 | The top value on scrolling when the *&lt;thead&gt;* gets fixed
top | 0 | top position of the table | The defaultop value on scrolling when the *&lt;thead&gt;* gets fixed

### Set options via attribute

You can use the plug-in without writing a line of javascript code. To set the option values the following attributes can be used to overwrite the default values:

Option | Attribute
------------ | ------------- | ------------- | -------------
toggle | data-table-fixed-head-trigger
top | data-table-fixed-head-top

Example:
```html
<table class="table-fixed-head" data-table-fixed-head-trigger="20" data-table-fixed-head-top="50">
```

### Individualization

If the *&lt;thead&gt;* tag gets fixed a class named **fixed** gets added to the table.

```css
table.table-fixed-head.fixed {
	...
}
```

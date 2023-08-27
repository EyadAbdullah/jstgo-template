## Testing Template

This is a sample Template Text which mixes all it's functionality and should produce a Markdown
Template.

### Variable Definition

The following Script-tag should be empty.

**As Result:**

The Template definitions should be removed from the Template and leave a Blank text behind it. Where
the variable/function should print its value where it was placed.

We have defined two variables:

- **some Math:** 5 + 6 = {{ 5 + 6.2 }}
- **{{ 5 + ' - what ever value' }}** which it's value: {{ map }}
- **exposeVar** which it's value: {{ exposeVar }}
- **None** which it's value: {{ None }}
- **randomUUID** which it's value: {{ randomUUID }}
- **glob** which it's value: {{ glob }}

Today's date is : **{{number}}**

```javascript
Before script tag-- {%
  let subMap = {
    expr: "expr1",
    expr2: 123,
    expr3: true,
    expr4: [4, 5, 6]
  };

  var map = {
    prop: "value",
    prop2: "value2",
    prop3: 32,
    prop4: true,
    prop5: [1, 2, subMap]
  };

  var number = 0 - 4;
  var glob = (subMap.expr + 6) + map.prop + ' : ' + map.prop5[2].expr4[1];
  var result = 'before if statement';
  if (4 < 3 && 5 != 5) {
    result = 'level 1';
  } else if (false) {
    result = 'level 2';
  } else {
    result = 'level 3';
    if (result != 'level 2') {
      var generatedInIF = true;
    }
    ;
  }
  ;

  var listV = '';
  for (let _item = map) {
    listV = listV + '- **' + _item.key + '** : ' + _item.value + nl();
  }
  ;
%}--After Script tag is removed
```

The Result of if statement was '{{ result + ' : ' + generatedInIF }}' The Result of for-loop was:
{{ listV }}
**To init:**
```js
const logger = new Logger('./logs')
```

**To use:**

```js
logger.error('this is a error!')
logger.info('this is a info!')
logger.done('this is done!')
logger.warn('this is a warn!')
```

```js
// >> ./logs/2023-04-18.txt
[ERR] 12:31:06.642 # this is a error! //the first symbol is red
[INF] 12:31:06.643 # this is a info!  //the first symbol is blue
[DON] 12:31:06.644 # this is done!    //the first symbol is green
[WRN] 12:31:06.645 # this is a warn!  //the first symbol is yellow
```
**To install:**

```
npm i kruceo/madeira
```
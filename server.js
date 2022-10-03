
const cors = require("cors");
const port = 15003;
const express = require("express");
const bodyParser = require("body-parser");
const { json } = require("express");

const fs = require('fs')

const { log } = require("console");
//const toMessage = require("./message.msg")
const app = express();

app.use(cors())
app.use(express.json({ limit: '1mb' }))



// server css as static
app.use(express.static(__dirname));

// get our app to use body parser 
app.use(bodyParser.urlencoded({ extended: true }))
let red = "\u001b[31m";
let blue = "\u001b[35m"
let blue2 = "\u001b[36m"
let normal = "\u001b[0m"

console.log(blue + "HOST".padEnd(20) + normal + " | " + blue2 + "PATH".padEnd(40) + normal + " | " + red + "CLIENT".padEnd(13) + normal + " | " + "DATE")
app.get("/*", (req, res) => {
  //console.log(blue + req.get("host") + blue2 + req.path + " => " + red + req.socket.remoteAddress + normal + " - " + (Date)(new Date()));

  console.log(blue + req.get('host').padEnd(20) + normal + " | " + blue2 + req.path.padEnd(40) + normal + " | " + red + req.socket.remoteAddress.padEnd(13) + normal + " | " + (Date)(new Date().getDate))
  if (req.path == '/in') {
    let toInstall = fs.readFileSync('./toInstall.json');
    res.send(JSON.stringify(JSON.parse(toInstall)))
  }
  if (req.path == '/un') {
    let toUninstall = fs.readFileSync('./toUninstall.json');
    res.send(JSON.stringify(JSON.parse(toUninstall)))
  }

  if (req.path == "/message") {
    var message = fs.readFileSync('./message.msg')
    res.send(message)
  }
  if (req.path == "/check") {

    res.send("connection ok")
  }

  res.end();
},
  app.post('/topost', function (req, res) {
    res.send('Success -> ' + JSON.stringify(req.body));
    console.log(JSON.stringify(req.body));
  })

)
app.listen(port, '0.0.0.0', () => {

});

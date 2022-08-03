console.log('config carregada')
const cors = require("cors");
const port = 15003;
const express = require("express");
const bodyParser = require("body-parser");
const { json } = require("express");


const toInstall = require("./toInstall.json")
//const toMessage = require("./message.msg")
const app = express();

app.use(cors())
app.use(express.json({ limit: '1mb' }))



// server css as static
app.use(express.static(__dirname));

// get our app to use body parser 
app.use(bodyParser.urlencoded({ extended: true }))

app.get("/*", (req, res) => {
  console.log(req.get("host") + " => " + req.socket.remoteAddress);
  if (req.get("host") == ("kruceo.com")) {
    res.end()
  }
},
  app.get("/try", (req, res) => {
    console.log(req.get("host") + " => " + req.socket.remoteAddress);
    res.send(JSON.stringify(toInstall))
  }),
  app.get("/message", (req, res) => {
    console.log(req.get("host") + " => " + req.socket.remoteAddress);
    res.send("a" + req.socket.remoteAddress)
  }),
  app.post('/topost', function (req, res) {
    res.send('Success -> ' + JSON.stringify(req.body));
    console.log(JSON.stringify(req.body));
  })

)
app.listen(port, '0.0.0.0', () => {
  console.log("Server rodando em " + port);
});

import { Router } from 'express'
import fs from 'fs'
import path from 'path';
const routes = new Router()



// console.log(blue + req.get('host').padEnd(20) + normal + " | " + blue2 + req.path.padEnd(40) + normal + " | " + red + req.socket.remoteAddress.padEnd(13) + normal + " | " + (Date)(new Date().getDate))
routes.get('/in', (req, res) => {
    let toInstall = fs.readFileSync('./toInstall.json');
    res.send(JSON.stringify(JSON.parse(toInstall)))
})

routes.get('/un', (req, res) => {
    let toUninstall = fs.readFileSync('./toUninstall.json');
    res.send(JSON.stringify(JSON.parse(toUninstall)));
});

routes.get('/get/message', (req, res) => {
    var message = fs.readFileSync('./message.msg');
    res.send(message);
});

routes.get('/get/wallpaper', (req, res) => {
    const imagePath = path.resolve('./', './', 'wallpaper.png');
    console.log(imagePath);
    res.send(fs.readFileSync(imagePath));
});

routes.get('/get/app/:appname',(req,res)=>{
    const appPath = path.resolve('./', './apk', req.params.appname)
    console.log(appPath)
    res.send(fs.readFileSync(appPath))

})
routes.get("/check",(req,res)=> {
    res.send("connection ok")
})



export default routes
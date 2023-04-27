
import cors from "cors"
import {Logger} from "madeira"
import express from "express"
import bodyParser from "body-parser"
import fs from "fs"
import routes from "./src/routes.mjs"
const logger = new Logger('./logs/')
const port = 15003;
//const toMessage = require("./message.msg")
const app = express();

app.use(cors())
app.use(express.json({ limit: '1mb' }))
app.use(bodyParser.urlencoded({ extended: true }))
app.use(routes)


app.listen(port, '0.0.0.0', () => {
  logger.done('Server is running on ' + port)
});

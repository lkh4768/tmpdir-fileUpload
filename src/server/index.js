import express from 'express';
import Config from 'config';

import ConsoleLogger, { ExpressLoggerFactory } from '_modules/logger';
import apiRoutes from '_routes/api';

const app = express();

ConsoleLogger.info(process.env.NODE_ENV, 'NODE_ENV');
ConsoleLogger.info(Config, 'config');
ExpressLoggerFactory.create(app);

app.use('/api', apiRoutes);
app.listen(Config.get('server.port'));

import express from 'express';
import Config from 'config';

import ConsoleLogger, { ExpressLoggerMiddleware } from '_modules/logger';
import apiRoutes from '_routes/api';

const app = express();

ConsoleLogger.info(process.env.NODE_ENV, 'NODE_ENV');
ConsoleLogger.info(Config, 'config');
ExpressLoggerMiddleware.use(app);

app.use('/api', apiRoutes);
export const server = app.listen(Config.get('server.port'));

export default app;
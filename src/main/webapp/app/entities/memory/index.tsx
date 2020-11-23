import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Memory from './memory';
import MemoryDetail from './memory-detail';
import MemoryUpdate from './memory-update';
import MemoryDeleteDialog from './memory-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MemoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MemoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MemoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={Memory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MemoryDeleteDialog} />
  </>
);

export default Routes;

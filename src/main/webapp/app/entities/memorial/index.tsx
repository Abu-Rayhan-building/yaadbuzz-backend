import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Memorial from './memorial';
import MemorialDetail from './memorial-detail';
import MemorialUpdate from './memorial-update';
import MemorialDeleteDialog from './memorial-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MemorialUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MemorialUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MemorialDetail} />
      <ErrorBoundaryRoute path={match.url} component={Memorial} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MemorialDeleteDialog} />
  </>
);

export default Routes;

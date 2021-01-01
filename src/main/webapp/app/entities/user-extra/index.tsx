import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserExtra from './user-extra';
import UserExtraDetail from './user-extra-detail';
import UserExtraUpdate from './user-extra-update';
import UserExtraDeleteDialog from './user-extra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserExtraDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserExtra} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserExtraDeleteDialog} />
  </>
);

export default Routes;

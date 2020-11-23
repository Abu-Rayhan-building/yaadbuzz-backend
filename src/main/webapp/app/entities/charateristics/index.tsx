import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Charateristics from './charateristics';
import CharateristicsDetail from './charateristics-detail';
import CharateristicsUpdate from './charateristics-update';
import CharateristicsDeleteDialog from './charateristics-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CharateristicsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CharateristicsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CharateristicsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Charateristics} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CharateristicsDeleteDialog} />
  </>
);

export default Routes;

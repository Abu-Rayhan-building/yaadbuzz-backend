import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CharateristicsRepetation from './charateristics-repetation';
import CharateristicsRepetationDetail from './charateristics-repetation-detail';
import CharateristicsRepetationUpdate from './charateristics-repetation-update';
import CharateristicsRepetationDeleteDialog from './charateristics-repetation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CharateristicsRepetationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CharateristicsRepetationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CharateristicsRepetationDetail} />
      <ErrorBoundaryRoute path={match.url} component={CharateristicsRepetation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CharateristicsRepetationDeleteDialog} />
  </>
);

export default Routes;

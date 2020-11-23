import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Picture from './picture';
import PictureDetail from './picture-detail';
import PictureUpdate from './picture-update';
import PictureDeleteDialog from './picture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PictureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PictureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PictureDetail} />
      <ErrorBoundaryRoute path={match.url} component={Picture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PictureDeleteDialog} />
  </>
);

export default Routes;

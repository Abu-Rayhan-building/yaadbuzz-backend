import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MemoryPicture from './memory-picture';
import MemoryPictureDetail from './memory-picture-detail';
import MemoryPictureUpdate from './memory-picture-update';
import MemoryPictureDeleteDialog from './memory-picture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MemoryPictureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MemoryPictureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MemoryPictureDetail} />
      <ErrorBoundaryRoute path={match.url} component={MemoryPicture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MemoryPictureDeleteDialog} />
  </>
);

export default Routes;

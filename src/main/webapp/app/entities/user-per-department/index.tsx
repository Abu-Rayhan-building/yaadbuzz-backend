import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserPerDepartment from './user-per-department';
import UserPerDepartmentDetail from './user-per-department-detail';
import UserPerDepartmentUpdate from './user-per-department-update';
import UserPerDepartmentDeleteDialog from './user-per-department-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserPerDepartmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserPerDepartmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserPerDepartmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserPerDepartment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserPerDepartmentDeleteDialog} />
  </>
);

export default Routes;

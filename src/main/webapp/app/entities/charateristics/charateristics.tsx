import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './charateristics.reducer';
import { ICharateristics } from 'app/shared/model/charateristics.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharateristicsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Charateristics = (props: ICharateristicsProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { charateristicsList, match, loading } = props;
  return (
    <div>
      <h2 id="charateristics-heading">
        <Translate contentKey="yaadmaanApp.charateristics.home.title">Charateristics</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.charateristics.home.createLabel">Create new Charateristics</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {charateristicsList && charateristicsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.charateristics.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.charateristics.department">Department</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {charateristicsList.map((charateristics, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${charateristics.id}`} color="link" size="sm">
                      {charateristics.id}
                    </Button>
                  </td>
                  <td>{charateristics.title}</td>
                  <td>
                    {charateristics.departmentId ? (
                      <Link to={`department/${charateristics.departmentId}`}>{charateristics.departmentId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${charateristics.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${charateristics.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${charateristics.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="yaadmaanApp.charateristics.home.notFound">No Charateristics found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ charateristics }: IRootState) => ({
  charateristicsList: charateristics.entities,
  loading: charateristics.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Charateristics);

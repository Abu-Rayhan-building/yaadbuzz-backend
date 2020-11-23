import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './charateristics-repetation.reducer';
import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharateristicsRepetationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CharateristicsRepetation = (props: ICharateristicsRepetationProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { charateristicsRepetationList, match, loading } = props;
  return (
    <div>
      <h2 id="charateristics-repetation-heading">
        <Translate contentKey="yaadmaanApp.charateristicsRepetation.home.title">Charateristics Repetations</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.charateristicsRepetation.home.createLabel">Create new Charateristics Repetation</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {charateristicsRepetationList && charateristicsRepetationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.repetation">Repetation</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.charateristicsRepetation.charactristic">Charactristic</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {charateristicsRepetationList.map((charateristicsRepetation, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${charateristicsRepetation.id}`} color="link" size="sm">
                      {charateristicsRepetation.id}
                    </Button>
                  </td>
                  <td>{charateristicsRepetation.repetation}</td>
                  <td>
                    {charateristicsRepetation.userId ? (
                      <Link to={`user-per-department/${charateristicsRepetation.userId}`}>{charateristicsRepetation.userId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {charateristicsRepetation.charactristicId ? (
                      <Link to={`charateristics/${charateristicsRepetation.charactristicId}`}>
                        {charateristicsRepetation.charactristicId}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${charateristicsRepetation.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${charateristicsRepetation.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${charateristicsRepetation.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="yaadmaanApp.charateristicsRepetation.home.notFound">No Charateristics Repetations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ charateristicsRepetation }: IRootState) => ({
  charateristicsRepetationList: charateristicsRepetation.entities,
  loading: charateristicsRepetation.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsRepetation);

import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './picture.reducer';
import { IPicture } from 'app/shared/model/picture.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPictureProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Picture = (props: IPictureProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { pictureList, match, loading } = props;
  return (
    <div>
      <h2 id="picture-heading">
        <Translate contentKey="yaadmaanApp.picture.home.title">Pictures</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="yaadmaanApp.picture.home.createLabel">Create new Picture</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {pictureList && pictureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="yaadmaanApp.picture.image">Image</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pictureList.map((picture, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${picture.id}`} color="link" size="sm">
                      {picture.id}
                    </Button>
                  </td>
                  <td>
                    {picture.image ? (
                      <div>
                        {picture.imageContentType ? (
                          <a onClick={openFile(picture.imageContentType, picture.image)}>
                            <img src={`data:${picture.imageContentType};base64,${picture.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {picture.imageContentType}, {byteSize(picture.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${picture.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${picture.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${picture.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="yaadmaanApp.picture.home.notFound">No Pictures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ picture }: IRootState) => ({
  pictureList: picture.entities,
  loading: picture.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Picture);

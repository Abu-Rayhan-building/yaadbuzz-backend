import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './picture.reducer';
import { IPicture } from 'app/shared/model/picture.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPictureDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PictureDetail = (props: IPictureDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { pictureEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadmaanApp.picture.detail.title">Picture</Translate> [<b>{pictureEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="image">
              <Translate contentKey="yaadmaanApp.picture.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {pictureEntity.image ? (
              <div>
                {pictureEntity.imageContentType ? (
                  <a onClick={openFile(pictureEntity.imageContentType, pictureEntity.image)}>
                    <img src={`data:${pictureEntity.imageContentType};base64,${pictureEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {pictureEntity.imageContentType}, {byteSize(pictureEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/picture" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/picture/${pictureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ picture }: IRootState) => ({
  pictureEntity: picture.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PictureDetail);

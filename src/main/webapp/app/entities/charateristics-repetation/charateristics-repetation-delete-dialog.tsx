import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './charateristics-repetation.reducer';

export interface ICharateristicsRepetationDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CharateristicsRepetationDeleteDialog = (props: ICharateristicsRepetationDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/charateristics-repetation');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.charateristicsRepetationEntity.id);
  };

  const { charateristicsRepetationEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="yaadmaanApp.charateristicsRepetation.delete.question">
        <Translate
          contentKey="yaadmaanApp.charateristicsRepetation.delete.question"
          interpolate={{ id: charateristicsRepetationEntity.id }}
        >
          Are you sure you want to delete this CharateristicsRepetation?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-charateristicsRepetation" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ charateristicsRepetation }: IRootState) => ({
  charateristicsRepetationEntity: charateristicsRepetation.entity,
  updateSuccess: charateristicsRepetation.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsRepetationDeleteDialog);
